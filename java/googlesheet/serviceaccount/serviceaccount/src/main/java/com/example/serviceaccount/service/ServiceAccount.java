package com.example.serviceaccount.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceAccount {

    private final String APPLICATION_NAME = "HelloWorld";
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private String p12FilePath = "p12.p12";
    private String googleServiceAccount = "googlesheet@projectrr-403891.iam.gserviceaccount.com";

    private String spreadSheetId = "1GziMDOOyuKkHyAGu3-D88-FNPnCKOOMnZz1DrfvkUi4";

    private String tabName = "f6a8e1a9-db9a-44ee-8686-f1a927fd1806";

    private Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) {

        try {

            File p12 = new File(p12FilePath);

            List<String> SCOPES_ARRAY = Arrays.asList(
                    "https://www.googleapis.com/auth/drive.file",
                    "https://www.googleapis.com/auth/userinfo.email",
                    "https://www.googleapis.com/auth/userinfo.profile",
                    "https://docs.google.com/feeds",
                    "https://spreadsheets.google.com/feeds");

            var credential = new GoogleCredential.Builder()
                    .setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId(googleServiceAccount)
                    .setServiceAccountPrivateKeyFromP12File(p12)
                    .setServiceAccountScopes(SCOPES_ARRAY)
                    .build();

            credential.refreshToken();

            return credential;

        } catch (Exception e) {
            System.out.println(e);

            return null;
        }
    }



    public void main() throws GeneralSecurityException, IOException {

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Credential credential = getCredentials(HTTP_TRANSPORT);

        Sheets sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Read
        ValueRange response = sheets.spreadsheets().values()
                .get(spreadSheetId, tabName)
                .execute();

        List<List<Object>> values = response.getValues();

        System.out.println(values.toString());

        // Write
        List<ValueRange> data = new ArrayList<>();

        List<List<Object>> newValue = new ArrayList<>();

        for(int x = 0; x < values.size(); x++) {
            newValue.add(Arrays.asList("col1-" + x, "col2-" + x, "col3-" + x,"col4-" + x, "col5-" + x));
        }

        data.add(new ValueRange().setRange(tabName).setValues(newValue));

        BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                .setValueInputOption("RAW")
                .setData(data);

        BatchUpdateValuesResponse result = sheets.spreadsheets().values().batchUpdate(spreadSheetId, body).execute();
    }
}
