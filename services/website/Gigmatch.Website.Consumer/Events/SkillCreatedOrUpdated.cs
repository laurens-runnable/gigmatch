namespace Gigmatch.Website.Consumer.Events
{
    internal class SkillCreatedOrUpdated
    {
        public Guid id { get; set; }
        public string name { get; set; }
        public string slug { get; set; }
    }
}