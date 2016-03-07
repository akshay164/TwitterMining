/* this is a highly customizable twitter miner. Not recommended for general mining. If you just want to fetch json files
 and clean later, use the other scripts (Python or R) in the git repository. */
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.io.*;
import java.util.UUID;
import java.util.Arrays;
import java.util.Date;
import java.lang.String;
import java.text.SimpleDateFormat;


public class Twitterak {

	public static void main(String[] args) throws TwitterException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("put consumer key here");
		cb.setOAuthConsumerSecret("put consumer secret here");
		cb.setOAuthAccessToken("put access token here");
		cb.setOAuthAccessTokenSecret("put access token secret here");
		cb.setIncludeEntitiesEnabled(true);
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		
		StatusListener listener = new StatusListener() {
			String textCheck;
			String textLang;
		
			public void onStatus(Status status) {
				
				
					
				
				String text1 = textClean(status.getText());
				if (textCheck != text1) {	//to reduce retweets
					textCheck = text1;
					UUID id1 = UUID.randomUUID();
					String hash1 = Arrays.toString(status.getHashtagEntities());
					hash1 = hashClean(hash1);
					//String description = textClean(status.getUser().getDescription());
					
					
					String url1 = Arrays.toString(status.getURLEntities());
					url1 = urlClean(url1);
					
					Date date = status.getCreatedAt();
					SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("YYYY-MM-dd");
					SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("hh:mm:ss");
					String date1 = DATE_FORMAT1.format(date);
					String date2 = DATE_FORMAT2.format(date);
					
					
					String lang = status.getLang();	
					switch (lang) {
					case "en":{ textLang = "\"text_en\": \"";
								break;
					}
					
					case "ru":{ textLang = "\"text_ru\": \"";
					break;
					}
					case "de":{ textLang = "\"text_de\": \"";
					break;
					}
					case "fr":{ textLang = "\"text_fr\": \"";
					break;
					}
					default:{ textLang = "\"text_general\": \"";	//insert case for remaining language
					break;
					}
					}

					try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tweet23en.txt", true)))) {	//put filename of txt file here
						// the following code, prints specific fields into json format into a txt file
						out.println("{\n\"uuid\": \"" + id1 + "\",\n"
								+ textLang + text1 + "\",\n" 
								+ "\"created_at\": \"" + date1 +"T"+ date2 +"Z" +"\",\n"
								+ "\"is_retweet\": \"" + status.isRetweet() + "\",\n"
								+ "\"lang\": \"" + status.getLang() + "\",\n" 
								+ "\"id\": \"" + status.getId() + "\",\n"
								+ "\"screen_name\": \"" + status.getUser().getScreenName() + "\",\n" 
								//+ "\"retweet_count\": \"" + status.getRetweetCount() + "\",\n" 
								+ "\"tweet_hashtags\": " + hash1 + ",\n" 
								+ "\"tweet_urls\": " + url1 + ",\n" 
								//+ "\"user_mentions\": " + userMention + ",\n"
								//+ "\"retweeted\": \"" + status.isRetweeted() + "\"\n"
								+ "\"followers\": \"" + status.getUser().getFollowersCount() + "\",\n"
								//+ "\"description\": \"" + description + "\",\n"
								+ "\"name\": \"" + status.getUser().getName() + "\",\n"
								+ "\"user_id\": \"" + status.getUser().getId() + "\",\n"
								+ "\"fav_count\": \"" + status.getUser().getFavouritesCount() + "\",\n"
								+ "\"location\": \"" + status.getUser().getLocation() + "\"\n},");
						
					} catch (IOException e) {
						System.out.println("Error");
					}
				}
				else{
					System.out.println(text1);
				}
			
			}
			public String textClean(String tweet) {
				tweet = tweet.replace("\"", "\\\"");
				tweet = tweet.replace("\n", " ");
				return tweet;

			}

			public String hashClean(String hash1) {
				hash1 = hash1.replaceAll("[={}]", "");
				hash1 = hash1.replace("HashtagEntityJSONImpl", "");
				hash1 = hash1.replace("text", "");
				hash1 = hash1.replace("'", "\"");
				return hash1;
			}

			public String urlClean(String url1) {
				url1 = url1.replaceAll("[{=}]", "");
				url1 = url1.replace("'", "\"");
				url1 = url1.replace("url", "");
				url1 = url1.replace("expandedURL", "");
				url1 = url1.replace("displayURL", "");
				url1 = url1.replace("URLEntityJSONImpl", "");

				return url1;
			}
		

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onStallWarning(StallWarning arg0) {

			}
			
		};

		FilterQuery fq = new FilterQuery();
		String keywords[] = {"теннис", "спортивный", "zenit st petersburg", "цска москва",
				"Fußball", "DFB-pokal", "Eishockey", "Boxen", "Deutsche Tourenwagen Masters", "bundesliga", "Bayern München","Vfl Wolfsburg" ,"Borussia Dortmund", 
				"Tischtennis", "Reitsport", "das Spiel","premier league", "champions league","yankees",
				 "chelsea", "manchester united", 
				"formula1", "new york giants", "buffalo bills" }; //put keywords here

		fq.track(keywords);
		fq.language("en");		//put the language here.

		twitterStream.addListener(listener);
		twitterStream.filter(fq);

	}

}
