namespace Gigmatch.Website.Consumer.Events
{
    public class VacancyOpened
    {
        public Guid id { get; set; }
        public string jobTitle { get; set; }
        public Experience[] experience { get; set; }
        public DateTime start { get; set; }
        public DateTime end { get; set; }
        public int rateAmount { get; set; }
        public RateType rateType { get; set; }
        public DateTime deadline { get; set; }
    }

    public class Experience
    {
        public Guid skillId { get; set; }
        public ExperienceLevel level { get; set; }
    }

    public enum ExperienceLevel
    {
        JUNIOR,
        MEDIOR,
        SENIOR
    }

    public enum RateType
    {
        HOURLY,
        DAILY,
        FIXED
    }
}