package com.example.win.a2vent;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by win on 2017-07-27.
 */

public class ServerConnector {

    private final String TAG = "테스트";

    private StringBuffer postDataBuilder;

    // 데이터 구분문자
    private final String boundary = "^******^";

    // 데이터 경계선
    private final String delimiter = "\r\n--" + boundary + "\r\n";

    HttpURLConnection httpURLConnection;

    private DataOutputStream dataOutputStream;
    private FileInputStream fileInputStream;

    public ServerConnector(String phpPage) {
        try {
            String serverURL = GlobalData.getURL() + phpPage;
            URL url = new URL(serverURL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep_Alive");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            postDataBuilder = new StringBuffer();
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(httpURLConnection.getOutputStream()));
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
    }

    public String response() throws IOException {
        int responseStatusCode = httpURLConnection.getResponseCode();
        Log.d(TAG, "POST response code - " + responseStatusCode);

        InputStream inputStream;
        if (responseStatusCode == httpURLConnection.HTTP_OK) {
            inputStream = httpURLConnection.getInputStream();
        } else {
            inputStream = httpURLConnection.getErrorStream();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();

        return sb.toString();
    }

    public void addPostData(String key, String value) {
        postDataBuilder.append(delimiter);
        postDataBuilder.append(setValue(key, value));
    }

    public void addDelimiter() {
        postDataBuilder.append(delimiter);
    }

    public void addFileData(String key, String fileName)  {
        postDataBuilder.append(delimiter);
        postDataBuilder.append(setFile(key, fileName));
        postDataBuilder.append("\r\n");
    }

    public void writePostData() throws IOException {
        Log.d(TAG, "data: " + postDataBuilder.toString());
        dataOutputStream.writeUTF(postDataBuilder.toString());
    }

    public void writeFileData(String uploadPath) throws IOException {
        Log.d(TAG, "data: " + postDataBuilder.toString());
        dataOutputStream.writeUTF(postDataBuilder.toString());

        try {
            fileInputStream = new FileInputStream(uploadPath);

            // 파일 복사 작업 시작
            int maxBufferSize = 1024;
            int bufferSize = Math.min(fileInputStream.available(), maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // 버퍼 크기만큼 파일로부터 바이트 데이터를 읽는다
            int byteRead = fileInputStream.read(buffer, 0, bufferSize);

            // 전송
            while (byteRead > 0) {
                dataOutputStream.write(buffer);
                bufferSize = Math.min(fileInputStream.available(), maxBufferSize);
                byteRead = fileInputStream.read(buffer, 0, bufferSize);
            }
        } catch (FileNotFoundException e) {

        }

        dataOutputStream.writeBytes(delimiter);
    }

    public void finish() throws IOException {
        dataOutputStream.flush();
        dataOutputStream.close();
        if (fileInputStream != null) {
            fileInputStream.close();
        }
    }

    private String setValue(String key, String value) {
        return "Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + value;
    }

    private String setFile(String key, String fileName) {
        return "Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + fileName + "\"\r\n";
    }
}
