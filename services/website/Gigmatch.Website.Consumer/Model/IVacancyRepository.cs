using Gigmatch.Website.Model;

namespace Gigmatch.Website.Consumer.Model;

internal interface IVacancyRepository
{
    Task SaveVacancyAsync(Vacancy vacancy);

    Task DeleteAllVacanciesAsync();
}