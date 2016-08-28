import requests
from bs4 import BeautifulSoup 
import Spider
import pdb
ROOT = 'http://bengali.oneindia.com/'

import re 
# This will clean the unwanted text from the url.
def cleanText(txt):
    #Remove add in between [ ...]
    return txt

def parse_artical(url):
    try:
        #Build the soup from URL
        soup = Spider.buildSoup(url)
        if soup == None :
            return None
        
        #define the prory to retrive from soup.
        info2 = {
                    'title':['h1.entry-title','text','NO_LIST_IF_ONE'],
                    'date':['.updated','text'],
                    'details':['.entry p','text','JOIN'],
                    'images':['img','src'],
                    'video':['iframe','src']
                }
                
        #Get the info from soup.
        x = Spider.getAttrListForXPath(soup,'.mh-content', None,info2)
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
            #Please check this. Bug prone..
            new  = [ n[:n.rfind('?')+1] for n in new ] 
            info['images'] = new
            info['head_image'] = new[0]
        
        info['details'] = cleanText(info['details'] )
        #we need to modify images
        
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
        x1 = Spider.getAttrListForXPath(soup,'.hp-main .slides .slide-wrap', None,{'url':['a','href']})
        
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
        x1 = Spider.getAttrListForXPath(soup,'.mh-main .loop-title', None,{'url':['a','href']})
        
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
    #i = parse_first_page_list("http://www.healthbarta.com/")
    #i = parse_artical_list("http://www.healthbarta.com/womens-health/");
    i = parse_artical("http://www.healthbarta.com/2016/08/24/11662/");
    print i
    pdb.set_trace()
    
    
#test();
 
 


