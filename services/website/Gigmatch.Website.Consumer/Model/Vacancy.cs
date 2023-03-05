namespace Gigmatch.Website.Consumer.Model;

internal class Vacancy
{
    public Guid Id { get; set; }

    public string JobTitle { get; set; }
    public Guid[] Skills { get; set; }

    public DateTime Start { get; set; }

    public DateTime End { get; set; }
}