package com.example.qlchitieu.data.db.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public abstract class BaseFirebase<T> {
    protected FirebaseFirestore firebaseDB;

    public BaseFirebase() {
        this.firebaseDB = FirebaseFirestore.getInstance();
    }

    protected abstract String getCollectionName();

    public void addDocument(String documentId,T data, DataCallback<String> callback){
        firebaseDB.collection(getCollectionName())
                .document(documentId)
                .set(data)
                .addOnSuccessListener(doc -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure("Error: " + e.getMessage()));
    }

    public void deleteDocument(String documentId, DataCallback<Void> callback){
        firebaseDB.collection(getCollectionName())
                .document(documentId)
                .delete()
                .addOnSuccessListener(doc -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateDocument(String documentId,T data,DataCallback<Void> callback){
        firebaseDB.collection(getCollectionName())
                .document(documentId)
                .set(data)
                .addOnSuccessListener(doc -> callback.onSuccess(null))
                .addOnFailureListener(e-> callback.onFailure(e.getMessage()));
    }

    public void getDocument(String docId, Class<T> clazz, DataCallback<T> callback) {
        firebaseDB.collection(getCollectionName())
                .document(docId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        callback.onSuccess(doc.toObject(clazz));
                    } else {
                        callback.onFailure("Document không tồn tại");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getAll(Class<T> clazz, DataCallback<List<T>> callback) {
        firebaseDB.collection(getCollectionName())
                .get()
                .addOnSuccessListener(query -> callback.onSuccess(query.toObjects(clazz)))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getWhere(
            String field,
            Object value,
            Class<T> clazz,
            DataCallback<List<T>> callback
    ) {
        firebaseDB.collection(getCollectionName())
                .whereEqualTo(field, value)
                .get()
                .addOnSuccessListener(query -> callback.onSuccess(query.toObjects(clazz)))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onFailure(String message);
    }
}