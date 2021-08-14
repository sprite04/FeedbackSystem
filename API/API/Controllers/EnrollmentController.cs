using API.Models;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class EnrollmentController : ApiController
    {
        [HttpGet]
        public List<Enrollment> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            //Lấy ra những lớp chưa được xoá và có số lượng sinh viên trong lớp lớn hơn 0
            List<Class> classes = context.Classes.Where(x => x.Trainees.Count > 0 && x.IsDeleted == false ).OrderBy(x => x.ClassID).ToList();
            List<Enrollment> enrollments = new List<Enrollment>();

            foreach (var x in classes)
            {
                foreach (var y in x.Trainees)
                {
                    if (y.IsActive == true)
                    {
                        enrollments.Add(new Enrollment(x.ClassID, y.UserName, x, y));                        
                    }

                }
            }
            
            foreach(var x in enrollments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Answers.Clear();
                x.Class.Trainees.Clear();
                x.Class.Assignments.Clear();
                x.Trainee.Answers.Clear();
                x.Trainee.Trainee_Comment.Clear();
                x.Trainee.Classes.Clear();
                x.Trainee.Trainee_Assignment.Clear();
            }
            return enrollments;
        }


        [HttpGet]
        public List<Enrollment> GetEnrollmentByClassId(int idClass)
        {
            SystemFeedback context = new SystemFeedback();
            //Lấy ra những lớp chưa được xoá và có số lượng sinh viên trong lớp lớn hơn 0
            List<Class> classes = context.Classes.Where(x => x.Trainees.Count > 0 && x.IsDeleted == false && x.ClassID == idClass).OrderBy(x => x.ClassID).ToList();
            List<Enrollment> enrollments = new List<Enrollment>();

            foreach (var x in classes)
            {
                foreach (var y in x.Trainees)
                {
                    if (y.IsActive == true)
                    {
                        enrollments.Add(new Enrollment(x.ClassID, y.UserName, x, y));
                    }

                }
            }

            foreach (var x in enrollments)
            {
                x.Class.Trainee_Comment.Clear();
                x.Class.Answers.Clear();
                x.Class.Trainees.Clear();
                x.Class.Assignments.Clear();
                x.Trainee.Answers.Clear();
                x.Trainee.Trainee_Comment.Clear();
                x.Trainee.Classes.Clear();
                x.Trainee.Trainee_Assignment.Clear();
            }
            return enrollments;
        }

        //Thêm trainee vào lớp
        [HttpPost]
        public bool AddTraineeToClass(int classId, String username)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class cl = context.Classes.FirstOrDefault(x => x.ClassID == classId && x.IsDeleted == false);
                Trainee trainee = context.Trainees.FirstOrDefault(x => x.UserName == username && x.IsActive == true);
                if(cl!=null && trainee!=null)
                {
                    int count = cl.Trainees.Where(x => x.UserName == trainee.UserName).Count();
                    if (count==0)
                    {

                        cl.Trainees.Add(trainee);
                        context.Entry(cl).State = EntityState.Modified;
                        context.SaveChanges();
                        return true;
                    }    
                    
                }                 
            }
            catch { }
            return false;
        }

        [HttpPut]
        public bool ChangeClass(int classIdOld,int classIdNew, String username)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class clOld = context.Classes.FirstOrDefault(x => x.ClassID == classIdOld && x.IsDeleted == false);
                Class clNew = context.Classes.FirstOrDefault(x => x.ClassID == classIdNew && x.IsDeleted == false);
                Trainee trainee = context.Trainees.FirstOrDefault(x => x.UserName == username && x.IsActive == true);
                if(clNew!=null && clOld!=null && trainee!=null )
                {
                    int countNew = clNew.Trainees.Where(x => x.UserName == trainee.UserName).Count();
                    int countOld = clOld.Trainees.Where(x => x.UserName == trainee.UserName).Count();
                    int count = clNew.Trainees.Count();
                    int capacity = clNew.Capacity ?? int.MaxValue;
                    if (countNew == 0 && countOld > 0 && count < capacity)
                    {
                        clNew.Trainees.Add(trainee);
                        context.Entry(clNew).State = EntityState.Modified;
                        clOld.Trainees.Remove(trainee);
                        context.Entry(clOld).State = EntityState.Modified;
                        context.SaveChanges();
                        return true;
                    }

                }   
            }
            catch { }

            return false;
        }

       


        [HttpDelete]
        public bool DeleteEnrollment(int classId, String username)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Class cl = context.Classes.FirstOrDefault(x => x.ClassID == classId && x.IsDeleted == false);
                Trainee trainee = context.Trainees.FirstOrDefault(x => x.UserName == username && x.IsActive == true);
                if (cl != null && trainee != null)
                {
                    int count = cl.Trainees.Where(x => x.UserName == trainee.UserName).Count();
                    if (count > 0)
                    {
                        cl.Trainees.Remove(trainee);
                        context.Entry(cl).State = EntityState.Modified;
                        context.SaveChanges();
                        return true;
                    }

                }

            }
            catch { }

            return false;
        }
    }
}
