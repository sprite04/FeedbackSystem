using API.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class ModuleController : ApiController
    {
        [HttpGet]
        public List<Module> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Module> modules = context.Modules.Where(x => x.IsDeleted == false && x.Feedback.IsDeleted==false).ToList();
            foreach (var x in modules)
            {
                x.Answers.Clear();
                x.Trainee_Comment.Clear();
                x.Assignments.Clear();
                x.Admin.Feedbacks.Clear();
                x.Admin.Modules.Clear();
                if(x.Feedback.Admin!=null)
                {
                    x.Feedback.Admin.Modules.Clear();
                    x.Feedback.Admin.Feedbacks.Clear();
                }    
                if(x.Feedback.TypeFeedback!=null)
                {
                    x.Feedback.TypeFeedback.Feedbacks.Clear();
                }    
                
                x.Feedback.Modules.Clear();
                x.Feedback.Questions.Clear();
                
            }
            return modules;
        }

        [HttpGet]
        public Module GetModuleById(int id)
        {
            SystemFeedback context = new SystemFeedback();
            Module module = context.Modules.FirstOrDefault(x => x.ModuleID == id && x.IsDeleted == false && x.Feedback.IsDeleted == false);
            if (module != null)
            {
                module.Answers.Clear();
                module.Trainee_Comment.Clear();
                module.Assignments.Clear();
                module.Admin.Feedbacks.Clear();
                module.Admin.Modules.Clear();
                module.Feedback.Modules.Clear();
                module.Feedback.Questions.Clear();
                module.Feedback.Admin.Feedbacks.Clear();
                module.Feedback.Admin.Modules.Clear();
                module.Feedback.TypeFeedback.Feedbacks.Clear();

            }
            return module;
        }

        [HttpGet]
        public List<Module> GetModuleByIdTrainer(String idTrainer)
        {
            SystemFeedback context = new SystemFeedback();
            List<Module> modules = new List<Module>();
            Trainer trainer = context.Trainers.FirstOrDefault(x => x.UserName == idTrainer && x.IsActive == true);
            if (trainer != null)
            {
                List<Assignment> assignments = context.Assignments.Where(x => x.TrainerID == idTrainer && x.Module.IsDeleted == false && x.Class.IsDeleted == false).ToList();

                foreach (var x in assignments)
                {
                    bool exist = false;
                    foreach (var y in modules)
                    {
                        if (x.ModuleID == y.ModuleID)
                        {
                            exist = true;
                            break;
                        }

                    }
                    if (exist == false)
                    {
                        modules.Add(x.Module);
                    }
                }
                foreach (var x in modules)
                {
                    x.Answers.Clear();
                    x.Trainee_Comment.Clear();
                    x.Assignments.Clear();
                    x.Admin.Feedbacks.Clear();
                    x.Admin.Modules.Clear();
                    if (x.Feedback.Admin != null)
                    {
                        x.Feedback.Admin.Modules.Clear();
                        x.Feedback.Admin.Feedbacks.Clear();
                    }
                    if (x.Feedback.TypeFeedback != null)
                    {
                        x.Feedback.TypeFeedback.Feedbacks.Clear();
                    }

                    x.Feedback.Modules.Clear();
                    x.Feedback.Questions.Clear();

                }
            }
            return modules;
        }


        //Lọc ra module của những lớp trainee tham gia nhưng lớp đó thoả mãn không delete và module không delete

        [HttpGet]
        public List<Module> GetModuleByIdTrainee(String idTrainee)
        {
            SystemFeedback context = new SystemFeedback();
            Trainee trainee = context.Trainees.FirstOrDefault(x => x.UserName == idTrainee && x.IsActive==true);
            List<Class> classes = trainee.Classes.Where(x=>x.IsDeleted==false).ToList();
            List<Module> moduleList = new List<Module>();
            foreach(var cl in classes)
            {
                List<Assignment> assignmentList = context.Assignments.Where(x => x.ClassID == cl.ClassID && x.Module.IsDeleted==false).ToList();
                foreach(var x in trainee.Trainee_Assignment)
                {
                    foreach(var y in assignmentList)
                    {
                        if(y.RegistrationCode==x.RegistrationCode)
                        {
                            bool exist = false;
                            foreach(var z in moduleList)
                            {
                                if(z.ModuleID==y.ModuleID)
                                {
                                    exist = true;
                                }    
                            }

                            if (exist == false)
                                moduleList.Add(y.Module);
                        }    
                    }
                }
            }
           
            foreach (var x in moduleList)
            {
                x.Answers.Clear();
                x.Trainee_Comment.Clear();
                x.Assignments.Clear();
                x.Admin.Feedbacks.Clear();
                x.Admin.Modules.Clear();
                if (x.Feedback.Admin != null)
                {
                    x.Feedback.Admin.Modules.Clear();
                    x.Feedback.Admin.Feedbacks.Clear();
                }
                if (x.Feedback.TypeFeedback != null)
                {
                    x.Feedback.TypeFeedback.Feedbacks.Clear();
                }

                x.Feedback.Modules.Clear();
                x.Feedback.Questions.Clear();

            }
            return moduleList;
        }

        [HttpGet]
        public bool CheckModuleUsed(int idUsed)
        {
            SystemFeedback context = new SystemFeedback();
            Module module = context.Modules.FirstOrDefault(x => x.ModuleID == idUsed && x.IsDeleted == false);
            if (module != null && module.StartTime!=null && module.EndTime!=null )
            {
                DateTime date =new DateTime(DateTime.Now.Year,DateTime.Now.Month,DateTime.Now.Day);
                if(date.CompareTo(module.StartTime)>=0&& date.CompareTo(module.EndTime) <= 0)
                {
                    return true;
                }
            }
            return false;
        }


        [HttpPost]
        public bool AddModule(Module m)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Module module = m;
                module.IsDeleted = false;
                context.Modules.Add(module);
                context.SaveChanges();
                return true;
            }
            catch { }
            return false;
        }


        //Cần test kỹ hàm này
        [HttpPut]
        public bool EditModule(Module m)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Module module = context.Modules.FirstOrDefault(x => x.ModuleID == m.ModuleID && x.IsDeleted == false);
                if (module != null)
                {
                    module.AdminID = m.AdminID;
                    module.ModuleName = m.ModuleName;
                    module.StartTime = m.StartTime;
                    module.EndTime = m.EndTime;
                    module.FeedbackStartTime = m.FeedbackStartTime;
                    module.FeedbackEndTime = m.FeedbackEndTime;
                    module.FeedbackID = m.FeedbackID;

                    context.Entry(module).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }

        [HttpDelete]
        public bool DeleteModule(int id)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Module module = context.Modules.FirstOrDefault(x => x.ModuleID == id && x.IsDeleted == false);
                if (module != null)
                {
                    module.IsDeleted = true;

                    context.Entry(module).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }

        

    }
}
