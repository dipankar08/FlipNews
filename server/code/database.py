import pymongo
import pdb
from pymongo import MongoClient

def printf(a):
    print a

def norm(a):
    del a['_id']
    return a;
    
class Store(object):
    __instance = None
    news = None
    reports = None
    def __new__(cls):
        if Store.__instance is None:
            Store.__instance = object.__new__(cls)
            client = MongoClient('localhost', 27018)
            db = client['flipnews_database']
            # Define Multiple Collection store
            # Keep adding any cooection you want to store here..
            cls.news = db['news_collection']
            cls.reports = db['reports_collection']
            printf('>>>>> DATA BASE newsECTION DONE <<<<< ')
            
        return Store.__instance
    def __init__(self):
        printf('callint __init__')
        pass
        
        
    ####################################################################################
    #  H E L P E R   F U N C T I O N S   F O R   N E W S  
    ####################################################################################
    def getnews(self):
        return Store.news
    
    #return True or False: tested
    def is_exist(self,url):
        return Store.news.find({"url": url}).count() > 0
    
    #Insert Unique URl : Tested
    def insert(self, data):
        url = data['url']
        if not self.is_exist(url):
            Store.news.insert(data)
            return True
        else:
            printf('Info: Alread exits : '+url)
            return False
            
    #Pagination get: Tested
    def get(self, page = 1,limit =10): #page 1...
        return [  norm(x)  for x in Store.news.find().sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit)]

    def getByCat(self, tags = 'calcutta', page = 1,limit =10): #page 1...  
        return [  norm(x)  for x in self.news.find({'categories' : tags }).sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit) ]

    def getBySource(self, tags = 'calcutta', page = 1,limit =10): #page 1...
        return [  norm(x)  for x in self.news.find({'source' : tags }).sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit) ]
        
    def getByDate(self, date = '08-10-2016', page = 1,limit =10): #page 1...
        #pdb.set_trace()
        return [  norm(x)  for x in self.news.find({'date' : date }).sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit) ]
        
        
    ####################################################################################
    #  H E L P E R   F U N C T I O N S   F O R   R E P O R T S  
    ####################################################################################
    def insert_crash(self, data):
        if True:
            Store.reports.insert(data)
            return True
        else:
            printf('Info: Alread exits : Just increase count')
            return False

    def get_crash(self, page = 1,limit =10): #page 1...
        return [  norm(x)  for x in Store.reports.find().sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit)]
        
    def search_crash(self,query="", page = 1,limit =10): #page 1...
        return [  norm(x)  for x in Store.reports.find().sort([("_id", pymongo.DESCENDING)]).skip(limit*(page-1)).limit(limit)]      
       

def test():   
    #Test single ton
    s1 = Store()
    c1 = s1.getnews()

    #Test Inserting
    obj = {'url':'abc','tags':'hello'}
    print s1.insert(obj)
    print s1.insert(obj)
    print s1.is_exist('abc')
    print s1.is_exist('abc1')

    #Test get and pagination
    for i in range(20):
        s1.insert({'url':'abc'+str(i),'tags':'hello'})
    print s1.get(1,3)
    print s1.get(2,3)
    print s1.get(3,3)
    print s1.get(4,3)
    print s1.get(5,3)
    print s1.get(6,3)

    #Test get by tagging..
    s1.insert({'url':'goo'+str(i),'tags':'goo'})
    s1.insert({'url':'foo'+str(i),'tags':'foo'})
    s1.insert({'url':'foo goo'+str(i),'tags':'foo goo'})

    print s1.getByCat('goo')
    print s1.getByCat('foo')
    print s1.getByCat('foo goo')
    print s1.getByCat('goo foo')
  
#pdb.set_trace()
