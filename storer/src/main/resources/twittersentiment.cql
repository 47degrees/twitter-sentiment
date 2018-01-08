CREATE KEYSPACE IF NOT EXISTS twittersentiment WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };
CREATE TABLE IF NOT EXISTS twittersentiment.tweet_message (message_id text, data text, PRIMARY KEY (message_id) );

