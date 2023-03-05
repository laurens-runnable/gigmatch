namespace Gigmatch.Website.Mvc.Models;

public class Vacancy
{
    public Guid Id { get; set; }

    public string JobTitle { get; set; }

    public DateTime Start { get; set; }

    public DateTime End { get; set; }
}