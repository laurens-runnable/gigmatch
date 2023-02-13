using SolTechnology.Avro;

namespace Gigmatch.Website.Consumer;

public class ModelGenerator
{
    private readonly string _avroPath;

    private readonly string _eventsPath;

    public ModelGenerator(string avroPath, string eventsPath)
    {
        _avroPath = avroPath;
        _eventsPath = eventsPath;
    }

    public void GenerateSource(string avroType)
    {
        var avscPath = $"{_avroPath}/{avroType}.avsc";
        if (!File.Exists(avscPath)) throw new FileNotFoundException($"Cannot find file {avscPath}");

        var avsc = File.ReadAllText(avscPath);
        var model = AvroConvert.GenerateModel(avsc);
        model = string.Join("\n", model.Split("\n")
            .Select(x => x.Trim())
            .Where(x => x.Length > 0)
            .Select(x => "    " + x));
        var source = "namespace Gigmatch.Website.Consumer.Events\n{\n" + model + "\n}\n";

        var destinationPath = $"{_eventsPath}/{avroType}.cs";
        Console.WriteLine($"Writing {destinationPath}");
        File.WriteAllText(destinationPath, source);
    }
}