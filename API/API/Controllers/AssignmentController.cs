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
    public class AssignmentController : ApiController
    {
        [HttpGet]
        public List<Assignment> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Assignment> assignments = context.Assignments.Where(x=>x.Class.IsDeleted==false && x.Module.IsDeleted==false && x.Trainer.IsActive==true).ToList();
            foreach (var x in assignments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Assignments.Clear();
                x.Class.Trainees.Clear();
                x.Class.Answers.Clear();
                x.Module.Feedback.Admin.Modules.Clear();
                x.Module.Feedback.Admin.Feedbacks.Clear();
                x.Module.Feedback.TypeFeedback.Feedbacks.Clear();
                x.Module.Feedback.Modules.Clear();
                x.Module.Feedback.Questions.Clear();
                x.Module.Admin.Modules.Clear();
                x.Module.Admin.Feedbacks.Clear();
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Trainer.Skill.Trainers.Clear();
                x.Trainer.Assignments.Clear();
            }
            return assignments;
        }

        [HttpGet]
        public List<Assignment> GetByModuleName(String stringSearch)
        {
            List<Assignment> assignments;
            SystemFeedback context = new SystemFeedback();
            if (!string.IsNullOrEmpty(stringSearch))
            {
                assignments = context.Assignments.Where(x => x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainer.IsActive == true && (x.Module.ModuleName.Contains(stringSearch) || x.Class.ClassName.Contains(stringSearch) || x.Trainer.Name.Contains(stringSearch) || x.RegistrationCode.Contains(stringSearch))).ToList();
            }
            else
            {
                assignments = context.Assignments.Where(x => x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainer.IsActive == true).ToList();
            }
            foreach (var x in assignments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Assignments.Clear();
                x.Class.Trainees.Clear();
                x.Class.Answers.Clear();
                x.Module.Feedback.Admin.Modules.Clear();
                x.Module.Feedback.Admin.Feedbacks.Clear();
                x.Module.Feedback.TypeFeedback.Feedbacks.Clear();
                x.Module.Feedback.Modules.Clear();
                x.Module.Feedback.Questions.Clear();
                x.Module.Admin.Modules.Clear();
                x.Module.Admin.Feedbacks.Clear();
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Trainer.Skill.Trainers.Clear();
                x.Trainer.Assignments.Clear();
            }
            return assignments;
        }

        [HttpGet]
        public List<Assignment> GetAssignmentByIdTrainer(String username)
        {
            SystemFeedback context = new SystemFeedback();
            //Cần xem lại có cần điều kiện để lấy danh sách assignment ra hay không
            //Trong trường hợp assignment có class, module đã xoá và trainer không active thì có lấy không
            List<Assignment> assignments = context.Assignments.Where(x=> x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainer.IsActive == true && x.Trainer.UserName==username).ToList();
            foreach (var x in assignments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Assignments.Clear();
                x.Class.Trainees.Clear();
                x.Class.Answers.Clear();
                x.Module.Feedback.Admin.Modules.Clear();
                x.Module.Feedback.Admin.Feedbacks.Clear();
                x.Module.Feedback.TypeFeedback.Feedbacks.Clear();
                x.Module.Feedback.Modules.Clear();
                x.Module.Feedback.Questions.Clear();
                x.Module.Admin.Modules.Clear();
                x.Module.Admin.Feedbacks.Clear();
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Trainer.Skill.Trainers.Clear();
                x.Trainer.Assignments.Clear();
            }
            return assignments;
        }

        [HttpGet]
        public List<Assignment> GetAssignmentByIdClass(int idClass)
        {
            SystemFeedback context = new SystemFeedback();
            //Cần xem lại có cần điều kiện để lấy danh sách assignment ra hay không
            //Trong trường hợp assignment có class, module đã xoá và trainer không active thì có lấy không
            List<Assignment> assignments = context.Assignments.Where(x => x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainer.IsActive == true && x.Class.ClassID==idClass).ToList();
            foreach (var x in assignments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Assignments.Clear();
                x.Class.Trainees.Clear();
                x.Class.Answers.Clear();
                x.Module.Feedback.Admin.Modules.Clear();
                x.Module.Feedback.Admin.Feedbacks.Clear();
                x.Module.Feedback.TypeFeedback.Feedbacks.Clear();
                x.Module.Feedback.Modules.Clear();
                x.Module.Feedback.Questions.Clear();
                x.Module.Admin.Modules.Clear();
                x.Module.Admin.Feedbacks.Clear();
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Trainer.Skill.Trainers.Clear();
                x.Trainer.Assignments.Clear();
                
            }
            return assignments;
        }

        [HttpGet]
        public List<Assignment> GetAssignmentByIdTrainee(String idTrainee)
        {
            SystemFeedback context = new SystemFeedback();
            Trainee trainee = context.Trainees.FirstOrDefault(x => x.UserName == idTrainee);
            List<Class> classes = trainee.Classes.Where(x => x.IsDeleted == false).ToList();
            List<Assignment> assignments = new List<Assignment>();
            List<Assignment> assignList = new List<Assignment>();
            DateTime date = DateTime.Now;

            foreach (var cl in classes)
            {
                List<Assignment> assignmentList = context.Assignments.Where(x => x.ClassID == cl.ClassID && x.Module.IsDeleted == false ).ToList();
                foreach(var x in assignmentList)
                {
                    if(x.Module.FeedbackStartTime!=null && x.Module.FeedbackEndTime!=null)
                    {
                        DateTime dateStart = x.Module.FeedbackStartTime ?? DateTime.Now;
                        DateTime dateEnd = x.Module.FeedbackEndTime ?? DateTime.Now;
                        if (x != null && date.CompareTo(dateStart) >= 0 && date.CompareTo(dateEnd) <= 0 && x.Module.Feedback.Questions.Count()>0)
                        {
                            x.Class.Trainee_Comment.Clear();
                            x.Class.Assignments.Clear();
                            x.Class.Trainees.Clear();
                            x.Class.Answers.Clear();
                            x.Module.Feedback.Admin.Modules.Clear();
                            x.Module.Feedback.Admin.Feedbacks.Clear();
                            x.Module.Feedback.TypeFeedback.Feedbacks.Clear();
                            x.Module.Feedback.Modules.Clear();
                            x.Module.Feedback.Questions.Clear();
                            x.Module.Admin.Modules.Clear();
                            x.Module.Admin.Feedbacks.Clear();
                            x.Module.Answers.Clear();
                            x.Module.Trainee_Comment.Clear();
                            x.Module.Assignments.Clear();
                            x.Trainer.Skill.Trainers.Clear();
                            x.Trainer.Assignments.Clear();
                            assignments.Add(x);
                        }
                    }    
                    
                }    
                


                foreach (var x in trainee.Trainee_Assignment)
                {
                    foreach (var y in assignments)
                    {
                        if (y.RegistrationCode == x.RegistrationCode)
                        {
                            bool exist = false;
                            foreach (var z in assignList)
                            {
                                if (z.ModuleID == y.ModuleID && z.ClassID==y.ClassID)
                                {
                                    exist = true;
                                }
                            }

                            if (exist == false)
                                assignList.Add(y);
                        }
                    }
                }
            }
            
            return assignList;
        }


        [HttpGet]
        public List<Assignment> GetByRegistrationCode(String registrationCode)
        {
            List<Assignment> assignments;
            SystemFeedback context = new SystemFeedback();

            assignments = context.Assignments.Where(x => x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainer.IsActive == true && x.RegistrationCode.Contains(registrationCode)).ToList();

            foreach (var x in assignments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Assignments.Clear();
                x.Class.Trainees.Clear();
                x.Class.Answers.Clear();
                x.Module.Feedback.Admin.Modules.Clear();
                x.Module.Feedback.Admin.Feedbacks.Clear();
                x.Module.Feedback.TypeFeedback.Feedbacks.Clear();
                x.Module.Feedback.Modules.Clear();
                x.Module.Feedback.Questions.Clear();
                x.Module.Admin.Modules.Clear();
                x.Module.Admin.Feedbacks.Clear();
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Trainer.Skill.Trainers.Clear();
                x.Trainer.Assignments.Clear();
            }
            return assignments;
        }

       


        //Tạo timestamp nên tạo ở dưới android -> vì vậy bước tạo code cũng tạo ở android -> cần kiểm tra xem class, assignment, trainer có tồn tại trong csdl hay k
        //Phân vân có nên thực hiện kiểm tra lại 1 lần nữa các giá trị đầu vào ở server hay k
        [HttpPost]
        public bool AddAssignment(Assignment agm)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class cl = context.Classes.FirstOrDefault(x => x.ClassID == agm.ClassID && x.IsDeleted == false);
                Module module = context.Modules.FirstOrDefault(x => x.ModuleID == agm.ModuleID && x.IsDeleted == false);
                Trainer trainer = context.Trainers.FirstOrDefault(x => x.UserName == agm.TrainerID && x.IsActive == true);
                Assignment assign = context.Assignments.FirstOrDefault(x => x.ClassID == agm.ClassID && x.ModuleID == agm.ModuleID);
                if(assign==null && cl!=null && module!=null && trainer!=null)
                {
                    Assignment assignment = agm;
                    context.Assignments.Add(assignment);
                    context.SaveChanges();
                    return true;
                }    
                

            }
            catch { }
            return false;
        }


        //Cần test kỹ hàm này
        //Phân vân có nên thực hiện kiểm tra lại 1 lần nữa các giá trị đầu vào ở server hay k
        [HttpPut]
        public bool EditAssignment(Assignment agm)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Assignment assignment = context.Assignments.FirstOrDefault(x => x.ModuleID==agm.ModuleID && x.ClassID==agm.ClassID && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainer.IsActive == true);
                if (assignment != null)
                {
                    Trainer trainer = context.Trainers.FirstOrDefault(x => x.UserName == agm.TrainerID && x.IsActive == true);
                    if(trainer!=null)
                    {
                        assignment.TrainerID = agm.TrainerID;

                        assignment.Trainer = null;

                        context.Entry(assignment).State = EntityState.Modified;
                        context.SaveChanges();
                        return true;
                    }
                    
                }    

            }
            catch { }

            return false;
        }


        public bool checkAssignmentUsed(int moduleId, int classId)
        {
            SystemFeedback context = new SystemFeedback();
            Assignment assignment = context.Assignments.FirstOrDefault(x => x.ModuleID == moduleId && x.ClassID==classId);
            if(assignment!=null && assignment.Module.StartTime!=null && assignment.Module.EndTime!=null && assignment.Class.StartTime!=null && assignment.Class.EndTime != null)
            {
                DateTime date = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day);
                DateTime startModule = assignment.Module.StartTime ?? date;
                DateTime endModule = assignment.Module.EndTime ?? date;
                DateTime startClass = assignment.Class.StartTime ?? date;
                DateTime endClass = assignment.Class.EndTime ?? date;
                if ((date.CompareTo(startModule) >= 0 && date.CompareTo(endModule) <= 0) || (date.CompareTo(startClass) >= 0 && date.CompareTo(endClass) <= 0))
                    return true;
            }
            return false;
        }


        [HttpDelete]
        public bool DeleteAssignment(int idClass, int idModule)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Assignment assignment = context.Assignments.FirstOrDefault(x => x.ModuleID==idModule && x.ClassID==idClass);
                if (assignment != null)
                {
                    context.Assignments.Remove(assignment);
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }
    }
}
