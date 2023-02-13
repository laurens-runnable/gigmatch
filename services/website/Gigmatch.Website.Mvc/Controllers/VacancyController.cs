using Gigmatch.Website.Mvc.Models;
using Microsoft.AspNetCore.Mvc;

namespace Gigmatch.Website.Mvc.Controllers;

[Route("/vacancies")]
public class VacancyController : Controller
{
    [HttpGet]
    public async Task<IActionResult> Index([FromServices] IVacancyRepository vacancyRepository)
    {
        var vacancies = await vacancyRepository.FindAllAsync(0, 10);
        var model = new VacancyIndexModel
        {
            Vacancies = vacancies
        };

        return View(model);
    }
}