using System.Text;
using Confluent.Kafka;
using Gigmatch.Website.Consumer.Events;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

namespace Gigmatch.Website.Consumer;

using MessageConsumer = IConsumer<Ignore, byte[]>;

public class ConsumerBackgroundService : BackgroundService
{
    private readonly ILogger<ConsumerBackgroundService> _logger;

    private readonly KafkaOptions _options;

    private readonly MessageConsumer _consumer;

    private readonly EventHandlerResolver _eventHandlerResolver;

    public ConsumerBackgroundService(ILogger<ConsumerBackgroundService> logger,
        IOptions<KafkaOptions> options, MessageConsumer consumer, EventHandlerResolver eventHandlerResolver)
    {
        _logger = logger;
        _options = options.Value;
        _consumer = consumer;
        _eventHandlerResolver = eventHandlerResolver;
    }

    protected override async Task ExecuteAsync(CancellationToken cancellationToken)
    {
        _logger.LogInformation("Subscribing to topic '{topic}'", _options.Topic);
        _consumer.Subscribe(_options.Topic);

        try
        {
            while (!cancellationToken.IsCancellationRequested)
            {
                var result = _consumer.Consume(cancellationToken);
                if (!result.Message.Headers.TryGetLastBytes("gm.type", out var bytes)) continue;

                var avroType = Encoding.UTF8.GetString(bytes);
                var eventHandler = _eventHandlerResolver(avroType);
                try
                {
                    if (eventHandler != null)
                    {
                        await eventHandler.HandleEventAsync(result.Message.Value);
                    }
                }
                catch (Exception ex)
                {
                    _logger.LogWarning("Error handling message of type '{type}': {message}", avroType, ex.Message);
                }
            }
        }
        catch (OperationCanceledException)
        {
        }
        finally
        {
            _logger.LogInformation("Unsubscribing from topic '{topic}'", _options.Topic);
            _consumer.Unsubscribe();
            _consumer.Close();
        }
    }
}