package fireIot.service;

interface INoticeService
{ 
   void scheduleNotice();
   void requestNotice();
   void clearNotice(int uid,int type);
}