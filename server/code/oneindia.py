import requests
from bs4 import BeautifulSoup 
import Spider
import pdb
ROOT = 'http://bengali.oneindia.com/'

import re 
# This will clean the unwanted text from the url.
def cleanText(txt):
    #Remove add in between [ ...]
    txt = re.sub(r'\[[^)]*\]', '', txt)
    return txt

def parse_artical(url):
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
        
        #define the prory to retrive from soup.
        info2 = {
                    'title':['h1.heading','text','NO_LIST_IF_ONE'],
                    'date':['.articlePublishDate','text'],
                    'details':['article p','text','JOIN'],
                    'images':['img','src'],
                    'video':['iframe','src']
                }
                
        #Get the info from soup.
        x = Spider.getAttrListForXPath(soup,'#container', None,info2)
        if x == None:
            return None
        info = x[0]
        
        #Make some modification on info.
        if info.get('images') != None:
            new =[]
            for f in info['images']:
                if f.startswith('/'):
                    new.append(ROOT+ f)
                else:
                    new.append(f)
            info['images'] = new
            info['head_image'] = new[0]
        
        info['details'] = cleanText(info['details'] )
        return info
    except Exception, e:
        print 'Error in (get_artical_info): ',url,str(e)
        return None

def parse_first_page_list(url):
    #pdb.set_trace();
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
            
        #Get the info from soup.
        x1 = Spider.getAttrListForXPath(soup,'div#collection-wrapper .collection-container', None,{'url':['a','href']})
        
        #Process:
        res =[ ]
        for link in x1:
            s = link.get('url')[0]
            if s.startswith('/'):
                res.append(ROOT+s)
            else:
                res.append(s)
                
        return res;        
    except Exception, e:
        print '[ERROR] parse_first_page',url,str(e)
        return None
            
def parse_artical_list(url):
    #pdb.set_trace();
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
            
        #Get the info from soup.
        x1 = Spider.getAttrListForXPath(soup,'div#collection-wrapper .collection-container', None,{'url':['a','href']})
        
        #Process:
        res =[ ]
        for link in x1:
            s = link.get('url')[0]
            if s.startswith('/'):
                res.append(ROOT+s)
            else:
                res.append(s)
                
        return res;        
    except Exception, e:
        print '[ERROR] parse_first_page',url,str(e)
        return None
       
        
def test():
    #i = get_artical_info('http://www.anandabazar.com/entertainment/i-didn-t-know-i-am-so-seductive-says-swastika-mukherjee-dgtl-1.459800#')
    #i = first_page_parser("http://www.anandabazar.com");
    #i = parse_first_page("http://bengali.oneindia.com/news/");
    i = parse_artical("http://bengali.oneindia.com//news/india/kashmir-unrest-mehbooba-mufti-will-meet-pm-modi-today-010029.html");
    print i
    pdb.set_trace()
    
    
#test();
 
 


