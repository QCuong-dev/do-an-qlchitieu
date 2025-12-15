package com.example.qlchitieu.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlchitieu.Adapter.MessageAdapter;
import com.example.qlchitieu.BuildConfig;
import com.example.qlchitieu.controller.CategoryController;
import com.example.qlchitieu.controller.TransactionController;
import com.example.qlchitieu.controller.WalletController;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.databinding.FragmentChatboxBinding;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.model.Message;
import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.Wallet;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatboxFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //My code
    private FragmentChatboxBinding binding;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private GenerativeModelFutures generativeModel;
    TransactionController transactionController;
    WalletController walletController;
    Helpers helper;


    public ChatboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatboxFragment newInstance(String param1, String param2) {
        ChatboxFragment fragment = new ChatboxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatboxBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. Khởi tạo RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        binding.recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewChat.setAdapter(messageAdapter);
        transactionController = new TransactionController(getContext());
        walletController = new WalletController(getContext());
        helper = new Helpers(getContext());

        // 2. Khởi tạo Gemini
        // Lấy API key từ BuildConfig
        GenerativeModel gm = new GenerativeModel(
                "gemini-2.5-flash",
                BuildConfig.GEMINI_API_KEY
        );
        generativeModel = GenerativeModelFutures.from(gm);

        // 3. Xử lý nút Gửi
        binding.buttonSend.setOnClickListener(v -> {
            String prompt = binding.editTextPrompt.getText().toString().trim();
            if (prompt.isEmpty()) {
                return;
            }

            // Thêm tin nhắn của user vào UI
            addMessage(prompt, true);
            binding.editTextPrompt.setText(""); // Xóa input

            // Gọi API Gemini
            callGeminiApi(buildSystemPrompt(prompt));
        });
    }

    private String buildTransactionContext(List<Transaction> list){
        StringBuilder sb = new StringBuilder();

        sb.append("- Danh sách giao dịch:\n");
        for(Transaction t : list){
            sb.append("- Ngày: ").append(t.getDate())
                    .append(", ID (Không hiển thị cho người dùng): ").append(t.getId())
                    .append(", Danh mục: ").append(t.getCategory_name())
                    .append(", Ghi chú: ").append(t.getNote())
                    .append(", Sốtiền: ").append(t.getAmount())
                    .append(", Loại: ").append(t.getType().equals("income") ? "Thu nhập" : "Chi tiêu")
                    .append("\n");
        }
        return sb.toString();
    }

    private String buildConversationContext(int maxMessages) {
        StringBuilder sb = new StringBuilder();
        sb.append("Lịch sử hội thoại gần đây:\n");

        int start = Math.max(0, messageList.size() - maxMessages);

        for (int i = start; i < messageList.size(); i++) {
            Message m = messageList.get(i);

            if (m.getText().length() < 3) continue; // bỏ "ok", "uh"

            sb.append(m.isUser() ? "Người dùng: " : "Trợ lý: ")
                    .append(m.getText())
                    .append("\n");
        }
        return sb.toString();
    }

    private String buildSystemPrompt(String userQuestion) {
        // Check wallet
        Wallet wallet = walletController.getWalletData();
        if(wallet == null){
            walletController.addWallet();
            wallet = walletController.getWalletData();
        }

        List<Transaction> list =
                transactionController.getAllHaveCategory();

        String conversationContext = buildConversationContext(6);
        String dataContext = buildTransactionContext(list);

        return
                "Giới thiệu:\n" +
                "- Bạn là trợ lý AI quản lý chi tiêu cá nhân.\n" +
                "- Dữ liệu được lấy từ ứng dụng quản lý chi tiêu offline.\n" +
                "- Nếu danh mục chưa tồn tại, vẫn trả tên danh mục để hệ thống tự xử lý.\n" +
                "- Hãy trả lời ngắn gọn, dễ hiểu bằng tiếng Việt.\n\n" +

                conversationContext + "\n" +
                "Ngày và giờ hôm nay: " + helper.getCurrentDate() + " " + helper.getCurrentTime() + "\n" +
                "Dữ liệu hiện tại:\n" +
                dataContext + "\n" +
                "- Ví hiện tại: " + wallet.getBalance() + " VND\n" +

                "Nhiệm vụ:\n" +
                "- Nếu người dùng muốn THÊM giao dịch, hãy trích xuất thông tin và trả về JSON.\n" +
                "- Nếu người dùng muốn SỬA giao dịch → trả JSON action update_transaction\n" +
                "- Nếu người dùng muốn XOÁ giao dịch → trả JSON action delete_transaction\n" +
                "- Nếu không phải thêm giao dịch, trả lời bình thường.\n\n" +

                "Định dạng JSON bắt buộc khi thêm|sửa|xóa giao dịch:\n" +
                "{\n" +
                "  \"action\": \"add_transaction|update_transaction|delete_transaction\",\n" +
                "  \"note\": \"\",\n" +
                "  \"category\": \"\",\n" +
                "  \"amount\": 0,\n" +
                "  \"type\": \"income | expense\",\n" +
                "  \"date\": \"yyyy-MM-dd\"\n" +
                "  \"time\": \"HH:mm\"\n" +
                "}\n\n" +

                "Luật:\n" +
                "- Khi UPDATE hoặc DELETE, phải xác định đúng transaction_id từ dữ liệu hiện tại\n" +
                "- Không được đoán transaction_id\n" +
                "- Chỉ dùng transaction_id có trong danh sách giao dịch\n\n" +
                "- Việc thêm 'Ví' vào là tổng chi tiêu một tháng của người dùng bạn dựa vào đó để đưa ra lời khuyên hợp lí dựa trên các giao dịch" +
                "- Chỉ trả JSON nếu là hành động thêm giao dịch.\n" +
                "- Không giải thích thêm ngoài JSON.\n\n" +
                "Luật bắt buộc:\n" +
                "- Nếu là thêm giao dịch, CHỈ trả về JSON thuần.\n" +
                "- KHÔNG markdown\n" +
                "- KHÔNG giải thích\n" +
                "- KHÔNG text trước hoặc sau\n" +
                "- KHÔNG emoji\n" +
                "Câu hỏi người dùng:\n" +
                userQuestion;
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }
        return null;
    }

    private void callGeminiApi(String prompt) {
        // Thêm tin nhắn "đang gõ..." của bot
        addMessage("Đang gõ...", false);

        // Tạo nội dung (prompt)
        Content content = new Content.Builder().addText(prompt).build();

        // Gọi API bất đồng bộ
        ListenableFuture<GenerateContentResponse> future = generativeModel.generateContent(content);
        Futures.addCallback(future, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String botResponse = result.getText();
                Log.d("AI_RAW", botResponse);
                String json = extractJson(botResponse);

                if(json != null){
                    handleAiCommand(json);
                }else{
                    updateBotMessage(botResponse);
                }
            }

            @Override
            public void onFailure(Throwable t) {

                t.printStackTrace();
                // Xử lý lỗi
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Log.e("GeminiError", "Lỗi đầy đủ: ", t);
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        // Xóa tin nhắn "đang gõ..." nếu bị lỗi
                        messageList.remove(messageList.size() - 1);
                        messageAdapter.notifyItemRemoved(messageList.size());
                    });
                }
            }
        }, Executors.newSingleThreadExecutor()); // Chạy trên một thread riêng
    }

    void handleAiCommand(String json){
        try {
            JSONObject obj = new JSONObject(json);
            String action = obj.optString("action", "");

            switch(action){
                case "add_transaction":
                    handleAddTransaction(obj);
                    break;
                case "update_transaction":
                    handleUpdateTransaction(obj);
                    break;

                case "delete_transaction":
                    handleDeleteTransaction(obj);
                    break;
                default:
                    updateBotMessage("Mình chưa hỗ trợ thao tác này.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateBotMessage("Mình không hiểu yêu cầu này. Error: " + e.getMessage());
        }
    }

    void updateBotMessage(String text){
        // Lấy tin nhắn cuối cùng (là tin nhắn "đang gõ...")
        int lastMessageIndex = messageList.size() - 1;
        Message typingMessage = messageList.get(lastMessageIndex);

        // Cập nhật trên UI Thread
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                // Sửa tin nhắn "đang gõ..." thành tin nhắn thật
                messageList.set(lastMessageIndex, new Message(text, false));
                messageAdapter.notifyItemChanged(lastMessageIndex);
                binding.recyclerViewChat.smoothScrollToPosition(lastMessageIndex);
            });
        }
    }

    void handleAddTransaction(JSONObject obj) throws JSONException {
        String categoryName = obj.getString("category");
        int amount = (int)obj.getDouble("amount");
        String note = obj.getString("note");
        String date = obj.getString("date");
        String time = obj.has("time") ? obj.getString("time") : "00:00";
        String type = obj.getString("type"); // tiền ra / tiền vào

        CategoryController categoryController =
                new CategoryController(getContext());

        TransactionController transactionController =
                new TransactionController(getContext());

        categoryController.saveCategory(categoryName, new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String msg) {
                String categoryUid =
                        categoryController.getCategoryUidByName(categoryName);

                if (categoryUid != null) {
                    saveTransaction(categoryUid);
                } else {
                    updateBotMessage("Không thể tạo danh mục.");
                }
            }

            @Override
            public void onFailure(String message) {
                String categoryUid =
                        categoryController.getCategoryUidByName(categoryName);

                if (categoryUid != null) {
                    saveTransaction(categoryUid);
                } else {
                    updateBotMessage("Không thể tạo danh mục.");
                }
            }

            private void saveTransaction(String categoryUid) {
                transactionController.saveTransaction(
                        amount,
                        categoryUid,
                        note,
                        date,
                        time,
                        type,
                        new BaseFirebase.DataCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                askAiToRespondSuccess(date, amount, categoryName);
                            }

                            @Override
                            public void onFailure(String message) {
                                updateBotMessage(message);
                            }
                        }
                );
            }
        });
    }

    void handleUpdateTransaction(JSONObject obj) throws JSONException {
        int transactionId = obj.getInt("transaction_id");
        String categoryName = obj.getString("category");
        int amount = obj.getInt("amount");
        String note = obj.getString("note");
        String date = obj.getString("date");
        String time = obj.getString("time");
        String type = obj.getString("type");

        CategoryController categoryController = new CategoryController(getContext());
        TransactionController transactionController = new TransactionController(getContext());

        String categoryUid = categoryController.getCategoryUidByName(categoryName);

        transactionController.updateTransaction(
                transactionId,
                amount,
                categoryUid,
                note,
                date,
                time,
                type,
                new BaseFirebase.DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        updateBotMessage("Đã cập nhật giao dịch thành công");
                    }

                    @Override
                    public void onFailure(String message) {
                        updateBotMessage(message);
                    }
                }
        );
    }

    void handleDeleteTransaction(JSONObject obj) throws JSONException {
        int transactionId = obj.getInt("transaction_id");

        TransactionController transactionController =
                new TransactionController(getContext());

        transactionController.deleteTransaction(
                transactionId,
                new BaseFirebase.DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        updateBotMessage("🗑 Đã xoá giao dịch");
                    }

                    @Override
                    public void onFailure(String message) {
                        updateBotMessage(message);
                    }
                }
        );
    }


    private void askAiToRespondSuccess(String note, int amount, String category) {
        String feedbackPrompt =
                "Đã thêm giao dịch:\n" +
                        "- " + note + "\n" +
                        "- " + amount + " VND\n" +
                        "- Danh mục: " + category;

        updateBotMessage(feedbackPrompt);
    }

    // Hàm helper để thêm tin nhắn vào list và cập nhật UI
    private void addMessage(String text, boolean isUser) {
        Message message = new Message(text, isUser);
        messageList.add(message);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        binding.recyclerViewChat.smoothScrollToPosition(messageList.size() - 1);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Tránh memory leak
    }
}