CREATE DATABASE SystemFeedback
GO

USE SystemFeedback
GO


--Username,Password yêu cầu chỉ chứa kí tự không bao gồm khoảng trắng, không được để trống

CREATE TABLE TypeFeedback
(
	TypeID INT IDENTITY(1,1),
	TypeName NVARCHAR(50),
	IsDeleted BIT,
	CONSTRAINT PK_TypeFB PRIMARY KEY(TypeID)
)
GO



CREATE TABLE Admin
(
	UserName NVARCHAR(50),
	Name NVARCHAR(255),
	Email NVARCHAR(50),
	Password NVARCHAR(255),
	CONSTRAINT PK_Admin PRIMARY KEY(UserName)
)
GO

CREATE TABLE Feedback
(
	FeedbackID INT IDENTITY(1,1),
	Title NVARCHAR(255),
	AdminID NVARCHAR(50),
	IsDeleted BIT,
	TypeFeedbackID INT,

	CONSTRAINT PK_FB PRIMARY KEY(FeedbackID),
	CONSTRAINT FK_FB_TypeFB FOREIGN KEY(TypeFeedbackID) REFERENCES dbo.TypeFeedback(TypeID),
	CONSTRAINT FK_FB_Admin FOREIGN KEY(AdminID) REFERENCES dbo.Admin(UserName)
)
GO

CREATE TABLE Topic
(
	TopicID INT IDENTITY(1,1),
	TopicName NVARCHAR(255),
	CONSTRAINT PK_Topic PRIMARY KEY(TopicID)
)
GO

CREATE TABLE Question
(
	QuestionID INT IDENTITY(1,1),
	TopicID INT,
	QuestionContent NVARCHAR(255),
	IsDeleted BIT,
	CONSTRAINT PK_Question PRIMARY KEY(QuestionID),
	CONSTRAINT FK_Question_Topic FOREIGN KEY(TopicID) REFERENCES dbo.Topic(TopicID)
)
GO


CREATE TABLE Feedback_Question
(
	FeedbackID INT,
	QuestionID INT,
	CONSTRAINT PK_FQ PRIMARY KEY(FeedbackID,QuestionID),
	CONSTRAINT FK_FQ_Feedback FOREIGN KEY(FeedbackID) REFERENCES dbo.Feedback(FeedbackID),
	CONSTRAINT FK_FQ_Question FOREIGN KEY(QuestionID) REFERENCES dbo.Question(QuestionID)
)
GO


CREATE TABLE Module
(
	ModuleID INT IDENTITY(1,1),
	AdminID NVARCHAR(50),
	ModuleName NVARCHAR(50),
	StartTime DATE,
	EndTime DATE,
	IsDeleted BIT,
	FeedbackStartTime DATETIME,
	FeedbackEndTime DATETIME,
	FeedbackID INT,
	CONSTRAINT PK_Module PRIMARY KEY(ModuleID),
	CONSTRAINT FK_Module_Admin FOREIGN KEY(AdminID) REFERENCES dbo.Admin(UserName),
	CONSTRAINT FK_Module_Feedback FOREIGN KEY(FeedbackID) REFERENCES dbo.Feedback(FeedbackID)
)
GO

CREATE TABLE Skill
(
	SkillID INT IDENTITY(1,1),
	Name NVARCHAR(50),
	CONSTRAINT PK_Skill PRIMARY KEY(SkillID)
)


CREATE TABLE Trainer
(
	UserName NVARCHAR(50),
	Name NVARCHAR(50),
	Email NVARCHAR(50),
	Phone NVARCHAR(50),
	Address NVARCHAR(255),
	IsActive BIT,
	Password NVARCHAR(255),
	IdSkill INT,
	ActivationCode NVARCHAR(50),
	ResetPasswordCode NVARCHAR(50),
	IsReceiveNotification BIT,
	CONSTRAINT PK_Trainer PRIMARY KEY(UserName),
	CONSTRAINT FK_Trainer_Skill FOREIGN KEY(IdSkill) REFERENCES dbo.Skill(SkillID)
)
GO



CREATE TABLE Class
(
	ClassID INT IDENTITY(1,1),
	ClassName NVARCHAR(255),
	Capacity INT,
	StartTime DATE,
	EndTime DATE,
	IsDeleted BIT,
	CONSTRAINT PK_Class PRIMARY KEY(ClassID)
)
GO


CREATE TABLE Trainee
(
	UserName NVARCHAR(50),
	Name NVARCHAR(50),
	Email NVARCHAR(50),
	Phone NVARCHAR(50),
	Address NVARCHAR(255),
	IsActive BIT,
	Password NVARCHAR(255),
	ActivationCode NVARCHAR(50),
	ResetPasswordCode NVARCHAR(50),
	CONSTRAINT PK_Trainee PRIMARY KEY(UserName)
)
GO

--cần xem lại chỗ mục classID, tại sao lại thiếu module id
CREATE TABLE Enrollment
(
	ClassID INT,
	TraineeID NVARCHAR(50),
	CONSTRAINT PK_Enrollment PRIMARY KEY(ClassID,TraineeID),
	CONSTRAINT FK_Enrollment_Trainee FOREIGN KEY(TraineeID) REFERENCES dbo.Trainee(UserName),
	CONSTRAINT FK_Enrollment_Class FOREIGN KEY(ClassID) REFERENCES dbo.Class(ClassID)
)
GO


--Xem các tham chiếu Class, Module, Trainee đến các bảng khác có cần phải làm hay không
CREATE TABLE Trainee_Comment
(
	ClassID INT,
	ModuleID INT,
	TraineeID NVARCHAR(50),
	Comment NVARCHAR(255),
	CONSTRAINT PK_TC PRIMARY KEY(ClassID,ModuleID,TraineeID),
	CONSTRAINT FK_TC_Class FOREIGN KEY(ClassID) REFERENCES dbo.Class(ClassID),
	CONSTRAINT FK_TC_Module FOREIGN KEY(ModuleID) REFERENCES dbo.Module(ModuleID),
	CONSTRAINT FK_TC_Trainee FOREIGN KEY(TraineeID) REFERENCES dbo.Trainee(UserName)
)
GO


--Vừa chỉnh lại database chú ý cập nhật lại
--Registation code: CL+[Class_Id]+M+[Module_Id]+T+[Timestamp]
CREATE TABLE Assignment
(
	--Hiển thị 1 cột đánh số No
	ClassID INT, --khi hiển thị trong giao diện thì hiển thị ClassName
	ModuleID INT, --Khi hiển thị trong giao diện assignment thì hiển thị ModuleName
	TrainerID NVARCHAR(50), --Khi hiển thị trong giao diện thì hiển thị trainer name
	RegistrationCode NVARCHAR(50), --Hiển thị theo mặc định
	CONSTRAINT PK_Assignment PRIMARY KEY(ClassID,ModuleID),
	CONSTRAINT FK_Assignment_Class FOREIGN KEY(ClassID) REFERENCES dbo.Class(ClassID),
	CONSTRAINT FK_Assignment_Module FOREIGN KEY(ModuleID) REFERENCES dbo.Module(ModuleID),
	CONSTRAINT FK_Assignment_Trainer FOREIGN KEY(TrainerID) REFERENCES dbo.Trainer(UserName)
)
GO


CREATE TABLE Answer
(
	ClassID INT,
	ModuleID INT,
	TraineeID NVARCHAR(50),
	QuestionID INT,
	Value INT,
	CONSTRAINT PK_Answer PRIMARY KEY(ClassID,ModuleID,TraineeID,QuestionID),
	CONSTRAINT FK_Answer_Class FOREIGN KEY(ClassID) REFERENCES dbo.Class(ClassID),
	CONSTRAINT FK_Answer_Module FOREIGN KEY(ModuleID) REFERENCES dbo.Module(ModuleID),
	CONSTRAINT FK_Answer_Trainee FOREIGN KEY(TraineeID) REFERENCES dbo.Trainee(UserName),
	CONSTRAINT FK_Answer_Question FOREIGN KEY(QuestionID) REFERENCES dbo.Question(QuestionID)
)
GO


--Cần xem lại thử Registration Code và Trainee ID có tham chiếu qua bảng nào hay không
CREATE TABLE Trainee_Assignment
(
	RegistrationCode NVARCHAR(50),
	TraineeID NVARCHAR(50),
	CONSTRAINT PK_TA PRIMARY KEY(RegistrationCode,TraineeID),
	CONSTRAINT FK_TA_Trainee FOREIGN KEY(TraineeID) REFERENCES dbo.Trainee(UserName)
)
GO












































GO







