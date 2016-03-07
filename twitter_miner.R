# ##### Twitter Mining through streamR library ##########
library(streamR)

requrl <- "https://api.twitter.com/oauth/request_token"
accurl <- "https://api.twitter.com/oauth/access_token"
authurl <- "https://api.twitter.com/oauth/authorize"
consecret <- "enter consumer secret here"
conkey <- "enter consumer key here" 
oauth <- OAuthFactory$new(consumerKey = conkey,
                          consumerSecret = consecret,
                          requestURL = requrl,
                          accessURL = accurl,
                          authURL = authurl)
oauth$handshake(cainfo = system.file("CurlSSL", "cacert.pem", package = "RCurl"))

save(oauth, file = "oauth.Rdata")

# tweet mining
load("oauth.Rdata")
filterStream(file.name = "twitterDatany.json", track = c("homes for sale", "house rental", "realtor", "housing", "foreclosure", "mortgage", "real estate", "apartment rental", "buying apartment", "house tax", "condominiums"), 
             language = "en", locations = c(-74, 40, -72, 41), tweets = 200, oauth = oauth)  # location <- east coast

# tweet importing
tweets <- parseTweets("twitterDatany.json", simplify = FALSE)

############## twitter mining through TwitteR package ################
library(twitteR)
library(RJSONIO)

consumer_key <- ""
consumer_secret<- ""
access_token <- ""
access_secret <- ""
#enter above details

tracker<- "Sanders OR Clinton OR Trump OR Rubio OR Kasich" #add keywords for search here.

setup_twitter_oauth(consumer_key, consumer_secret, access_token = access_token, access_secret = access_secret)

data <- searchTwitter(searchString = tracker, n=20, since ="2016-02-20") #n is number of tweets to capture.
data <- twListToDF(data) # to convert into data frame
# use RJSONIO to convert DF to json and vice-versa, by using functions toJSON and fromJSON
