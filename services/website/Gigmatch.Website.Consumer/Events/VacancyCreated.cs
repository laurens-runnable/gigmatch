namespace Gigmatch.Website.Consumer.Events
{
    public class VacancyCreated
    {
        public Guid id { get; set; }
        public string jobTitle { get; set; }
        public string[] skills { get; set; }
        public DateTime start { get; set; }
        public DateTime end { get; set; }
        public int rateAmount { get; set; }
        public RateType rateType { get; set; }
        public DateTime deadline { get; set; }
        public bool listed { get; set; }
    }

    public enum RateType
    {
        HOURLY,
        DAILY,
        FIXED
    }
}