namespace Gigmatch.Website.Consumer;

public class KafkaOptions
{
    public string[] Brokers { get; set; }
    public string Group { get; set; }
    public string Topic { get; set; }
}