package aws;

import java.io.File;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;

public class sheetal 
{
	
	AWSCredentials credentials=new BasicAWSCredentials("AKIARPHQ2KZF7TNCUTHQ","MHfvQs66pJjDbrhfn11Kbx/VwmaJG2fKdznNiqXK");
	public static void main(String gg[])
	{
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
		final AmazonTranscribe transcribe = AmazonTranscribeClient.builder().withRegion("us-east-2").build();
		String b_name="my2ndbucketom1";
		try
		{
			s3.createBucket(b_name);
		}
		catch(AmazonS3Exception e)
		{
			System.err.println(e.getErrorMessage());
		}
		
		
		// Upload file in bucket
		String key_name="eng_m1.wav";
		String file_Path="C:\\Users\\Gi\\Desktop\\Python\\EXCEL\\eng_m1.wav";
		try {
		s3.putObject(b_name,key_name,new File(file_Path));
		}
		catch(AmazonServiceException e)
		{
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}

		// create Transcription job
		StartTranscriptionJobRequest request = new StartTranscriptionJobRequest();
		request.withLanguageCode(LanguageCode.EnUS);
		Media media = new Media();
		media.setMediaFileUri(s3.getUrl(b_name,key_name).toString());
		request.withMedia(media).withMediaSampleRateHertz(44100);
		String transcriptionJobName = "fit";

		request.setTranscriptionJobName(transcriptionJobName);
		request.withMediaFormat("mp3");

		transcribe.startTranscriptionJob(request);
		
		GetTranscriptionJobRequest jobRequest = new GetTranscriptionJobRequest();
		jobRequest.setTranscriptionJobName(transcriptionJobName);
		TranscriptionJob transcriptionJob;
		String transcription;
		
		while(true)
		{
				transcriptionJob=transcribe.getTranscriptionJob(jobRequest).getTranscriptionJob();
				
				//if(transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name()))
				if(transcriptionJob.getTranscriptionJobStatus().equals("COMPLETED"))
				{
					// changes done
					transcription=transcriptionJob.getTranscript().getRedactedTranscriptFileUri();
					System.out.println("completed");
					System.out.println(transcription);
					break;
				}
				else if(transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {
					System.out.println("failed");
					break;
				}
		}
		
		/**privateAmazonTranscription download(String uri,String keyname) 
		{
			HttpResponse response = HttpRequest.get(uri).send();
			String result = response.charset("UTF-8").bodyText();
			return gson().fromJson(result, AmazonTranscription.class);
		}**/
		
	}
}
