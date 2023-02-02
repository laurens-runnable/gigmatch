using Microsoft.AspNetCore.Mvc;

namespace Gigmatch.Website.Mvc.Controllers;

[Route("/")]
public class HomeController : Controller
{
    [HttpGet]
    public IActionResult Index()
    {
        return View();
    }
}