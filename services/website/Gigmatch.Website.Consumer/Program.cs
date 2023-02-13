using Gigmatch.Website.Consumer;

var commandLineArgs = Environment.GetCommandLineArgs();
switch (commandLineArgs)
{
    case [_, "run"]:
    {
        var application = new Application("appsettings.json");
        application.Run();
        break;
    }
    case [_, "generate-model", _]:
    {
        var avroType = commandLineArgs[2];
        var modelGenerator = new ModelGenerator("./avro/events", "./Events");
        try
        {
            modelGenerator.GenerateSource(avroType);
        }
        catch (FileNotFoundException ex)
        {
            Console.WriteLine(ex.Message);
            Environment.Exit(-1);
        }

        break;
    }
    default:
        Console.WriteLine("Invalid arguments");
        Environment.Exit(-1);
        break;
}