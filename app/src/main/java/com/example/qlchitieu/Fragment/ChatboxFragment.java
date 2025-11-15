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
import com.example.qlchitieu.databinding.FragmentChatboxBinding;
import com.example.qlchitieu.model.Message;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;

import java.util.ArrayList;
import java.util.List;
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
            callGeminiApi(prompt);
        });
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
                // Lấy tin nhắn cuối cùng (là tin nhắn "đang gõ...")
                int lastMessageIndex = messageList.size() - 1;
                Message typingMessage = messageList.get(lastMessageIndex);

                // Cập nhật tin nhắn đó bằng nội dung thật từ Gemini
                String botResponse = result.getText();

                // Cập nhật trên UI Thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Sửa tin nhắn "đang gõ..." thành tin nhắn thật
                        messageList.set(lastMessageIndex, new Message(botResponse, false));
                        messageAdapter.notifyItemChanged(lastMessageIndex);
                        binding.recyclerViewChat.smoothScrollToPosition(lastMessageIndex);
                    });
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