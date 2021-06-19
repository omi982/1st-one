package aws;

import java.util.List;
import com.amazonaws.auth.presign.PresignerParams.Builder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

public class blist {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final AmazonS3 s3 =AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_EAST_2)
				.build();
		List<Bucket> buckets = s3.listBuckets();
		System.out.println("Your Amazon s3 buckets are:");
		for(Bucket b : buckets)
		{
			System.out.println("* "+b.getName());
		}

	}

}
