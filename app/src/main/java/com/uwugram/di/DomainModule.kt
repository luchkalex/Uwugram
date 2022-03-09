package com.uwugram.di

import com.uwugram.domain.repositories.UserRepository
import com.uwugram.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideGetChatsUsecase(userRepository: UserRepository): GetChatsUsecase {
        return GetChatsUsecase(userRepository)
    }

    @Provides
    fun provideGetContactsUsecase(userRepository: UserRepository): UpdateContactsUsecase {
        return UpdateContactsUsecase(userRepository)
    }

    @Provides
    fun provideGetUserUsecase(userRepository: UserRepository): GetUserUsecase {
        return GetUserUsecase(userRepository)
    }

    @Provides
    fun provideCheckAuthorizedUsecase(userRepository: UserRepository): CheckAuthorizedUsecase {
        return CheckAuthorizedUsecase(userRepository)
    }

    @Provides
    fun provideUpdateUserStateUsecase(userRepository: UserRepository): UpdateUserStateUsecase {
        return UpdateUserStateUsecase(userRepository)
    }

    @Provides
    fun provideSetUserImageUsecase(userRepository: UserRepository): SetUserImageUsecase {
        return SetUserImageUsecase(userRepository)
    }

    @Provides
    fun provideLogoutUsecase(userRepository: UserRepository): LogoutUsecase {
        return LogoutUsecase(userRepository)
    }

    @Provides
    fun provideSendVerificationCodeUsecase(userRepository: UserRepository): SendVerificationCodeUsecase {
        return SendVerificationCodeUsecase(userRepository)
    }

    @Provides
    fun provideVerifyCodeUsecase(userRepository: UserRepository): VerifyCodeUsecase {
        return VerifyCodeUsecase(userRepository)
    }

    @Provides
    fun provideGetContactsListUsecase(userRepository: UserRepository): GetContactsListUsecase {
        return GetContactsListUsecase(userRepository)
    }

    @Provides
    fun provideInitializeUserUsecase(userRepository: UserRepository): InitializeUserUsecase {
        return InitializeUserUsecase(userRepository)
    }

    @Provides
    fun provideSendMessageUsecase(userRepository: UserRepository): SendMessageUsecase {
        return SendMessageUsecase(userRepository)
    }

    @Provides
    fun provideGetContactInfoUsecase(userRepository: UserRepository): GetContactInfoUsecase {
        return GetContactInfoUsecase(userRepository)
    }

    @Provides
    fun provideGetMessagesUsecase(userRepository: UserRepository): GetMessagesUsecase {
        return GetMessagesUsecase(userRepository)
    }

    @Provides
    fun provideSignUpUsecase(userRepository: UserRepository): SignUpUsecase {
        return SignUpUsecase(userRepository)
    }

    @Provides
    fun provideUpdateFullNameUsecase(userRepository: UserRepository): UpdateFullNameUsecase {
        return UpdateFullNameUsecase(userRepository)
    }

    @Provides
    fun provideGetFullNameUsecase(userRepository: UserRepository): GetFullNameUsecase {
        return GetFullNameUsecase(userRepository)
    }

    @Provides
    fun provideUpdateUsernameUsecase(userRepository: UserRepository): UpdateUsernameUsecase {
        return UpdateUsernameUsecase(userRepository)
    }

    @Provides
    fun provideGetUsernameUsecase(userRepository: UserRepository): GetUsernameUsecase {
        return GetUsernameUsecase(userRepository)
    }

    @Provides
    fun provideUpdateBioUsecase(userRepository: UserRepository): UpdateBioUsecase {
        return UpdateBioUsecase(userRepository)
    }

    @Provides
    fun provideGetBioUsecase(userRepository: UserRepository): GetBioUsecase {
        return GetBioUsecase(userRepository)
    }
}