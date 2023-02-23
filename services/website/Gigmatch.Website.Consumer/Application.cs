using Gigmatch.Website.Consumer.Elastic;
using Gigmatch.Website.Consumer.Elastic.DependencyInjection;
using Gigmatch.Website.Consumer.Events.DependencyInjection;
using Gigmatch.Website.Consumer.Kafka;
using Gigmatch.Website.Consumer.Kafka.DependencyInjection;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace Gigmatch.Website.Consumer;

public class Application
{
    private readonly string _jsonFile;

    public Application(string jsonFile)
    {
        _jsonFile = jsonFile;
    }

    private void ConfigureServices(IServiceCollection services)
    {
        var config = new ConfigurationBuilder()
            .AddJsonFile(_jsonFile)
            .Build();

        services.AddLogging(options => options
            .AddConfiguration(config.GetSection("Logging"))
            .AddConsole());

        services.AddOptions();

        var configurationSection = config.GetSection("Kafka");
        services.Configure<KafkaOptions>(configurationSection);
        services.AddKafkaConsumer();

        services.Configure<ElasticOptions>(config.GetSection("Elastic"));
        services.AddElasticRepositories();

        services.AddEventHandlers();
        services.AddHostedService<ConsumerBackgroundService>();
    }

    public void Run()
    {
        var host = new HostBuilder()
            .ConfigureServices((_, services) => ConfigureServices(services))
            .UseConsoleLifetime()
            .Build();

        host.Run();
    }
}