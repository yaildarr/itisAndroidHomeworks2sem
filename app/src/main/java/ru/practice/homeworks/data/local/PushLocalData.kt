package ru.practice.homeworks.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PushLocalData @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("push_data", Context.MODE_PRIVATE)

    fun savePush(data: String){
        with(sharedPreferences.edit()){
           putString("data",data)
        }
    }
}