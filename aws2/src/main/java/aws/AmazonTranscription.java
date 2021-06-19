package aws;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobResult;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobResult;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;

public class AmazonTranscription 
{
	static
	{
	    System.setProperty("aws.accessKeyId", "AKIARPHQ2KZF7TNCUTHQ");
	    System.setProperty("aws.secretKey"  , "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");
	}
	private static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-2").
			withClientConfiguration(new ClientConfiguration()).withCredentials(new DefaultAWSCredentialsProviderChain() ).build();
	private static AmazonTranscribe client = AmazonTranscribeClient.builder().withRegion("us-east-2").build();
	
	public static void main(String[] args)
	{		
		final AmazonS3 s3 =AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_EAST_2)
				.build();
		String BUCKET_NAME = "my2ndbucketom";
		String fileName = "PTT-20210617-WA0028.opus";
		String file_path= "C:\\Users\\Gi\\Desktop\\Python\\EXCEL\\PTT-20210617-WA0028.opus";
		
		try
		{
			s3.putObject(BUCKET_NAME, fileName, new File(file_path));			
		}
		catch(AmazonServiceException e)
		{
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}	
		
			StartTranscriptionJobRequest request = new StartTranscriptionJobRequest();
		    request.withLanguageCode(LanguageCode.EnUS);
		    Media media = new Media();
		    media.setMediaFileUri(s3.getUrl(BUCKET_NAME, fileName).toString());
		    request.withMedia(media).withMediaSampleRateHertz(48000);		    
		    String transcriptionJobName = "0111stonetomoon";
		    request.setTranscriptionJobName(transcriptionJobName);
		    request.withMediaFormat("opus");
		    Settings s =  request.getSettings();
		    s.setShowSpeakerLabels(true);//error
		    s.setMaxSpeakerLabels(3);
		    //try {Settings s =  request.getSettings();s.setShowSpeakerLabels(true);s.setMaxSpeakerLabels(3);		    }
		    //catch(NullPointerException e) 		    {		  	System.out.println("Recaught:"+e);		    }
		    client.startTranscriptionJob(request);
		    
		    GetTranscriptionJobRequest result = new GetTranscriptionJobRequest();
		    result.setTranscriptionJobName(transcriptionJobName);
		    //GetTranscriptionJobResult response = client.getTranscriptionJob(result);
		    //System.out.println(response.getTranscriptionJob().getTranscript());
		    
		    TranscriptionJob transcriptionJob;

		    while( true )
		    {
		        transcriptionJob = client.getTranscriptionJob(result).getTranscriptionJob();
		        if( transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name()) )
		        {
		            read(transcriptionJob.getTranscript().getTranscriptFileUri());

		            break;
		        }
		        else if( transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name()) )
		        {
		        	break;
		        }
		        // to not be so anxious
		     
		    }
	}
	static void read(String uri)
	{
		try
		{
			URL obj = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			int responseCode = con.getResponseCode();
			System.out.println("Sending GET request to URL-"+uri);
			System.out.println("ResponseCode-"+responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
    //String uri = "https://my2ndbucketom1.s3.us-east-2.amazonaws.com/eng_m1.wav";   
	
}