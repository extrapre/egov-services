var kafka = require("kafka-node");

const Producer = kafka.Producer;
let client;

if (process.env.NODE_ENV === "development") {
  client = new kafka.Client(process.env.KAFKA_BOOTSTRAP_SERVER);
  console.log("local - ");
} else {
  client = new kafka.KafkaClient({ kafkaHost: "kafka-0.kafka.backbone:9092" });
  console.log("cloud - ");
}

const producer = new Producer(client);

producer.on("ready", function() {
  console.log("Producer is ready");
});

producer.on("error", function(err) {
  console.log("Producer is in error state");
  console.log(err);
});

export default producer;
