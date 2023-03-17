namespace Gigmatch.Website.Model;

public class Vacancy
{
    public class ExperienceItem
    {
        public Skill Skill { get; set; }

        public int Level { get; set; }
    }

    public Guid Id { get; init; }

    public string JobTitle { get; set; }

    public List<ExperienceItem> Experience { get; set; }

    public DateTime Start { get; set; }

    public DateTime End { get; set; }

    public DateTime Deadline { get; set; }
}