using Gigmatch.Website.Model;

namespace Gigmatch.Website.Mvc.Models;

public interface IVacancyRepository
{
    Task<IEnumerable<Vacancy>> FindAllAsync(int offset, int size);
}