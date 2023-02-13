namespace Gigmatch.Website.Consumer.Events;

public interface IEventHandler
{
    Task HandleEventAsync(byte[] data);
}

public delegate IEventHandler? EventHandlerResolver(string avroType);