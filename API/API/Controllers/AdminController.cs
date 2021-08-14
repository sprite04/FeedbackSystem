using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class AdminController : ApiController
    {
        [HttpGet]
        public List<Admin> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Admin> admins = context.Admins.ToList();
            foreach (var x in admins)
            {
                x.Modules.Clear();
                x.Feedbacks.Clear();
            }
            return admins;
        }

        [HttpGet]
        public Admin GetAdmin(String username, String password)
        {
            SystemFeedback context = new SystemFeedback();
            Admin admin = context.Admins.FirstOrDefault(x =>  x.UserName == username && x.Password == password);
            if (admin != null)
            {
                admin.Modules.Clear();
                admin.Feedbacks.Clear();
            }
            return admin;
        }
    }
}
