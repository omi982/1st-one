package aws;
//Imports the Google Cloud client library
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class QuickstartSample {

/** Demonstrates using the Speech API to transcribe an audio file. */
public static void main(String... args) throws Exception {
 // Instantiates a client
 try (SpeechClient speechClient = SpeechClient.create()) {

   // The path to the audio file to transcribe
   String gcsUri = "https://my2ndbucketom1.s3.us-east-2.amazonaws.com/eng_m1.wav";

   // Builds the sync recognize request
   RecognitionConfig config =
       RecognitionConfig.newBuilder()
           .setEncoding(AudioEncoding.LINEAR16)
           .setSampleRateHertz(8000)
           .setLanguageCode("en-US")
           .build();
   RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

   // Performs speech recognition on the audio file
   RecognizeResponse response = speechClient.recognize(config, audio);
   List<SpeechRecognitionResult> results = response.getResultsList();

   for (SpeechRecognitionResult result : results) {
     // There can be several alternative transcripts for a given chunk of speech. Just use the
     // first (most likely) one here.
     SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
     System.out.printf("Transcription: %s%n", alternative.getTranscript());
   }
 }
}
}