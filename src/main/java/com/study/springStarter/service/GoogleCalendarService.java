package com.study.springStarter.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "My Notion";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private NetHttpTransport httpTransport;

    public GoogleCalendarService(@Value("${google.client.id}") String clientId, @Value("${google.client.secret}") String clientSecret, @Value("${google.redirect.uri}") String redirectUri) throws GeneralSecurityException, IOException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    // Google OAuth2 인증 흐름을 생성
    private AuthorizationCodeFlow getAuthorizationCodeFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientId, clientSecret,
                Collections.singleton(CalendarScopes.CALENDAR_EVENTS)).
                setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

    // 사용자 인증을 위한 URL을 생성
    public String getAuthorizationUrl() throws IOException {
        AuthorizationCodeFlow flow = getAuthorizationCodeFlow();
        return flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .build();
    }

    // 인증 코드를 사용하여 Access Token과 Refresh Token을 얻고, 이를 Credential로 변환
    public Credential exchangeCodeForCredential(String code, String email) throws IOException {
        AuthorizationCodeFlow flow = getAuthorizationCodeFlow();
        TokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();
        return flow.createAndStoreCredential(tokenResponse, email);
    }

    public Calendar getCalendarClient(String email) throws IOException, GeneralSecurityException {
        Credential credential = getAuthorizationCodeFlow().loadCredential(email);
        if(credential == null) {
            return null;
        }
        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
