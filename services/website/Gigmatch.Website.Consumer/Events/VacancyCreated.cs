namespace Gigmatch.Website.Consumer.Events
{
    internal class VacancyCreated
    {
        public Guid id { get; set; }
        public string name { get; set; }
        public DateTime start { get; set; }
    }
}