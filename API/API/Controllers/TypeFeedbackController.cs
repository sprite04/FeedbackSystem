using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class TypeFeedbackController : ApiController
    {
        [HttpGet]
        public List<TypeFeedback> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<TypeFeedback> typeFeedbacks = context.TypeFeedbacks.Where(x => x.IsDeleted == false).ToList();
            foreach (var x in typeFeedbacks)
            {
                x.Feedbacks.Clear();
            }
            return typeFeedbacks;
        }
    }
}
