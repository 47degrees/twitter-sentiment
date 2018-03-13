# twitter-sentiment
Twitter Stream to Google PubSub and Google PubSub to Cassandra

This project showcases some usages of the services in the Google Cloud Platform and their integration with other components.

This is an SBT project developed in Scala featuring two modules:

- Module `reader`: It reads from a Twitter stream and submits into a Google PubSub topic.
- Module `storer`: It reads messages from a Google PubSub topic and persist them in Casssandra.

## Usage
* To run the reader module

  `sbt "project reader" "run"` 
* To run the storer module

  `sbt "project storer" "run"`

## Integration tests
`sbt it:test` 

We use the [docker-it-scala](https://github.com/whisklabs/docker-it-scala) library to pull a cassandra image running in docker with the same version that we use in runtime.
[cassandra-unit](https://github.com/jsevellec/cassandra-unit) is used to create the keyspaces in this cassandra instance.
