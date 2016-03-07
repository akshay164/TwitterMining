import twitter
import json
import sys
import pymongo
def save_to_mongo(data, mongo_db, mongo_db_coll, **mongo_conn_kw):
	client = pymongo.MongoClient(**mongo_conn_kw)
	db = client[mongo_db]
	coll = db[mongo_db_coll]
	return coll.insert(data)
def set_default(obj):
	if isinstance(obj, set):
		return list(obj)
	raise TypeError
CONSUMER_KEY=''  #Consumer key
CONSUMER_SECRET=''  #Consumer secret
OAUTH_TOKEN='' #Oauth token
OAUTH_TOKEN_SECRET='' #Oauth secret
auth = twitter.oauth.OAuth( OAUTH_TOKEN, OAUTH_TOKEN_SECRET,
CONSUMER_KEY, CONSUMER_SECRET)
twitter_api = twitter.Twitter( auth = auth)
q = 'iphone, android,mobile,smartphone,google,apple' #keywords to be searched, separated by comma
twitter_stream = twitter.TwitterStream( auth = twitter_api.auth) 
stream = twitter_stream.statuses.filter( track = q,language='en') #language to be searched, separated by comma
for tweet in stream:
	data={}
	if 'id' in tweet:
		data['id']=tweet['id']
	if 'created_at' in tweet:
		data['created_at']=tweet['created_at']
	if 'coordinates' in tweet:
		data['coordinates']=tweet['coordinates']
	if 'lang' in tweet:
		data['lang']=tweet['lang']
	if 'text' in tweet:
		data['text']=tweet['text'].encode('utf-8')
	print json.dumps(data['text'],indent=1)
	if 'favorite_count' in tweet:
		data['favorite_count']=tweet['favorite_count']
	if 'retweet_count' in tweet:
		data['retweet_count']=tweet['retweet_count']
	if 'user' in tweet:
		if 'id' in tweet['user']:
			data['user_id']=tweet['user']['id'];
			if 'name' in tweet['user']:
				data['screen_name']=tweet['user']['name'];
			if 'entities' in tweet:
				if 'hashtags' in tweet['entities']:
					data['hashtags']=tweet['entities']['hashtags'];
				if 'urls' in tweet['entities']:
					data['urls']=tweet['entities']['urls'];
	save_to_mongo(data,"Test","Proj1Data"); #Please create corresponding Collection and database name in Mongodb