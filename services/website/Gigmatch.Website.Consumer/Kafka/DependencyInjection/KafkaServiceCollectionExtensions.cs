using Confluent.Kafka;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;

namespace Gigmatch.Website.Consumer.Kafka.DependencyInjection;

internal static class KafkaServiceCollectionExtensions
{
    internal static IServiceCollection AddKafkaConsumer(this IServiceCollection services)
    {
        services.AddSingleton<IConsumer<Ignore, byte[]>>(s =>
        {
            var options = s.GetRequiredService<IOptions<KafkaOptions>>().Value;
            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = string.Join(",", options.Brokers),
                GroupId = options.Group,
                AutoOffsetReset = AutoOffsetReset.Earliest
            };
            return new ConsumerBuilder<Ignore, byte[]>(consumerConfig).Build();
        });
        return services;
    }
}