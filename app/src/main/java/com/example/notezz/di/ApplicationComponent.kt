package com.example.notezz.di

import android.content.Context
import com.example.notezz.authUi.LoginActivity
import com.example.notezz.authUi.SignupActivity
import com.example.notezz.authUi.WelcomeActivity
import com.example.notezz.ui.AddNoteActivity
import com.example.notezz.ui.ArchiveActivity
import com.example.notezz.ui.DeletedActivity
import com.example.notezz.ui.MainActivity
import com.example.notezz.ui.SearchActivity
import com.example.notezz.ui.SettingActivity
import com.example.notezz.ui.UpdateArchiveNote
import com.example.notezz.ui.UpdateNoteActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class , DatabaseModule::class , SharedPreferencesModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(addNoteActivity: AddNoteActivity)
    fun inject(archiveActivity: ArchiveActivity)
//    fun inject(deletedActivity: DeletedActivity)
//    fun inject(searchActivity: SearchActivity)
//    fun inject(settingActivity: SettingActivity)
    fun inject(updateArchiveNote: UpdateArchiveNote)
    fun inject(updateNoteActivity: UpdateNoteActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(signupActivity: SignupActivity)
    fun inject(welcomeActivity: WelcomeActivity)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : ApplicationComponent
    }
}