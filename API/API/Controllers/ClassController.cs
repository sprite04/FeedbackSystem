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
    public class ClassController : ApiController
    {
        [HttpGet]
        public List<Class> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Class> classes= context.Classes.Where(x => x.IsDeleted == false).OrderBy(x => x.ClassID).ToList();
            foreach (var x in classes)
            {
                x.Trainee_Comment.Clear();
                x.Answers.Clear();
                x.Trainees.Clear();
                x.Assignments.Clear();
            }
            
            return classes;
        }

        [HttpGet]
        public List<Class> GetClassesByTrainer(String idTrainer)
        {
            SystemFeedback context = new SystemFeedback();
            List<Class> classes = new List<Class>();
            Trainer trainer = context.Trainers.FirstOrDefault(x => x.UserName == idTrainer);
            if(trainer!=null)
            {
                if(trainer.Assignments.Count()>0)
                {
                    foreach (var x in trainer.Assignments)
                    {
                        classes.Add(x.Class);
                    }

                    foreach (var cl in classes)
                    {
                        cl.Trainee_Comment.Clear();
                        cl.Answers.Clear();
                        cl.Trainees.Clear();
                        cl.Assignments.Clear();
                    }
                }    
            }
            return classes;
        }

        [HttpGet]
        public List<Class> GetClassesByTrainee(String idTrainee)
        {
            SystemFeedback context = new SystemFeedback();
            List<Class> classes = new List<Class>();
            Trainee trainee = context.Trainees.FirstOrDefault(x => x.UserName == idTrainee);
            if (trainee != null)
            {
                if (trainee.Classes.Count() > 0)
                {
                    
                    foreach (var cl in trainee.Classes)
                    {
                        cl.Trainee_Comment.Clear();
                        cl.Answers.Clear();
                        cl.Trainees.Clear();
                        cl.Assignments.Clear();
                    }
                    classes.AddRange(trainee.Classes);
                }
            }
            return classes;
        }



        [HttpGet]
        public Class GetClassById(int id)
        {
            SystemFeedback context = new SystemFeedback();
            Class cl = context.Classes.FirstOrDefault(x => x.ClassID == id && x.IsDeleted == false);
            if (cl != null)
            {
                cl.Trainee_Comment.Clear();
                cl.Answers.Clear();
                cl.Trainees.Clear();
                cl.Assignments.Clear();
            }
            return cl;
        }

        [HttpPost]
        public bool AddClass(Class c)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class cl = c;
                cl.IsDeleted = false;
                context.Classes.Add(cl);
                context.SaveChanges();
                return true;
            }
            catch { }
            return false;
        }

        //Cần test kỹ hàm này
        [HttpPut]
        public bool EditClass(Class c)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class cl = context.Classes.FirstOrDefault(x => x.ClassID== c.ClassID && x.IsDeleted == false);
                if (cl != null)
                {
                    cl.ClassName = c.ClassName;
                    cl.Capacity = c.Capacity;
                    cl.StartTime = c.StartTime;
                    cl.EndTime = c.EndTime;
                  
                    context.Entry(cl).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }

        [HttpDelete]
        public bool DeleteClass(int id)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class cl = context.Classes.FirstOrDefault(x => x.ClassID == id && x.IsDeleted == false);
                if (cl != null)
                {
                    cl.IsDeleted = true;

                    //class là khoá 1 trong khoá chính của assignment nên xoá assignment thì assignment sẽ biến mất
                    cl.Assignments.Clear();
                    //Tương tự
                    cl.Answers.Clear();

                    context.Entry(cl).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }
    }
}
