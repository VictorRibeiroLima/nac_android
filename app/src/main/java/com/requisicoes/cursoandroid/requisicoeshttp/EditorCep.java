package com.requisicoes.cursoandroid.requisicoeshttp;

import android.text.Editable;
import android.text.TextWatcher;

public class EditorCep implements TextWatcher {
    private boolean isRunning = false;
    private boolean isDeleting = false;
    private final String mask;

    public EditorCep (String mask) {
        this.mask = mask;
    }

    public static EditorCep editaCep() {
        return new EditorCep("#####-###");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        isDeleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isRunning || isDeleting) {
            return;
        }
        isRunning = true;

        int editableLength = editable.length();
        if (editableLength < mask.length()) {
            if (mask.charAt(editableLength) != '#') {
                editable.append(mask.charAt(editableLength));
            } else if (mask.charAt(editableLength-1) != '#') {
                editable.insert(editableLength-1, mask, editableLength-1, editableLength);
            }
        }

        isRunning = false;
    }
}