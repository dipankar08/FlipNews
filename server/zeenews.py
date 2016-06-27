import requests
from bs4 import BeautifulSoup 
#Add your seed link here
import pdb
ROOT = 'http://zeenews.india.com'
master_config={
    'allowled':['zeenews'],
    'max_news_in_each_cata':6,
}

def get_artical_info(url): #Working..
    print 'getting',url
    html = requests.get(url)
    if html.status_code != 200:
        print 'Error: Can;t retrive artical'
        return None
    #pdb.set_trace();
    soup = BeautifulSoup(html.text)
     
    story_container =  soup.find("div", { "class" : "connrtund" }) 
 
    title = story_container.find('div',{'class':'full-story-head'}).text
    date =  story_container.find("div", { "class" : "writer" }).text
    details = ''.join( [ p.text for p in story_container.find("div", { "class" : "full-con" }).find_all('p')])
    #pdb.set_trace()
    images = [ m.get('src') for m in story_container.find("div", { "class" : "full-con" }).find_all('img') if m.get('src').endswith('jpg')]
    head_image = images[0] if len(images) > 0 else None
    return {'title':title,'date':date,'details':details,'images':images,'head_image':head_image}
      
 
def get_all_artical_links(url):
    try:
        print 'getting',url
        html = requests.get(url)
        if html.status_code != 200:
            print 'Error: Can;t retrive artical'
            return None
        #pdb.set_trace();
        soup = BeautifulSoup(html.text)
         
        links =[]
        #pdb.set_trace()
        x = soup.find("div", { "class" : "lead-health" })
        if x: links.append(x.find('a').get('href'))
 

        links += [ y.find('a').get('href') for y in soup.find_all("div", { "class" : "lead-health-nw" }) if y.find('a')]
        
        global ROOT
        return [ ROOT + l for l in links if l[0] == '/']
                  
         
    except Exception, e:
        print 'Error in Group',url,str(e)
        return None
 
 
#All Interface must implemnets this...
import random,string
def get_all_data_for_a_seed(seed):
    links = get_all_artical_links(seed['url'])
    if not links:
        return None
    res = []
    #pdb.set_trace()
    ii =0;
    for l in links:
        ii = ii+1
        if(ii > master_config['max_news_in_each_cata'] ):
            break;
        ares = get_artical_info(l)
        if not ares:
            continue
        ares['url'] = l
        ares['source'] = 'ZeeNews'
        ares['categories']= seed['categories']
        ares['tags']= seed['categories']
        ares['rand_id']= ''.join(random.SystemRandom().choice(string.ascii_uppercase + string.digits) for _ in range(20)) 
        res.append(ares)
    return res
 
 
         
#test Public API

