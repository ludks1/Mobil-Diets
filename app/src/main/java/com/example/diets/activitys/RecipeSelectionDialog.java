package com.example.diets.activitys;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diets.R;
import com.example.diets.record.RecipeResponse;

import java.util.List;

public class RecipeSelectionDialog extends DialogFragment {
    private List<RecipeResponse.Result> recipeList;
    private RecipeAdapter.OnRecipeClickListener listener;

    public RecipeSelectionDialog(List<RecipeResponse.Result> recipeList, RecipeAdapter.OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_recipe_selection, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recipeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecipeAdapter adapter = new RecipeAdapter(recipeList, recipe -> {
            listener.onRecipeClick(recipe);
            dismiss();
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}
