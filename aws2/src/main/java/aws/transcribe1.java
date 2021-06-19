package aws;

import java.io.File;
import java.net.http.HttpRequest;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

public class transcribe1 
{
	static{
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
		String BUCKET_NAME = "my2ndbucketom1";
		String fileName = "eng_m1.wav";
		String file_path= "C:\\Users\\Gi\\Desktop\\Python\\EXCEL\\eng_m1.wav";
		
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

		    request.withMedia(media).withMediaSampleRateHertz(8000);
		    
		    String transcriptionJobName = "21stonetomoon";
		    
		    request.setTranscriptionJobName(transcriptionJobName);
		    request.withMediaFormat("wav");

		    client.startTranscriptionJob(request);
		    
		    String uri = "https://my2ndbucketom1.s3.us-east-2.amazonaws.com/eng_m1.wav";
		    
		    private AmazonTranscription download(uri,fileName)
		    {
		        HttpResponse response = HttpRequest.get(uri).send();
		        String result = response.charset("UTF-8").bodyText();
		        // result is a json 
		        return gson.fromJson(result, AmazonTranscription.class);
		    }
	}
}
