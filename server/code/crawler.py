from sourceConfig import config
from masterConfig import mconfig
import pdb
import pickle
import database
import time 
def CrawlAll():
    allres =[]
    #pdb.set_trace();
    for k,v in config.items():
        if k not in mconfig['allowled']:
            continue
        seeds = v['seeds']
        handaler = v['handaler']
        for sd in seeds:
            data = handaler(sd)
            if data:
                allres += data
                
    print 'Total', len(allres); print '*'*50; 
    return allres

def ProcessData(dj):
    dj['tags' ] = dj['categories']
    dj['epoch' ] = int(time.time())
    return dj;
    
def UpdateDataBase():
    store = database.Store();
    data = CrawlAll();
    count =0;
    ig_count =0;
    if data:
        for dj in data:
            #Process Something here ...
            dj = ProcessData(dj)
            # End processing....
            if store.insert(dj):
                count = count +1;
            else:
               ig_count = ig_count+1;
    print '*'*50;
    print 'Total Updates on database:', count
    print 'Total duplicates on database:', ig_count
    print '*'*50;

def save(allres):
    with open('data.pkl', 'wb') as handle:
        pickle.dump(allres, handle)

        
#CrawlAll();
