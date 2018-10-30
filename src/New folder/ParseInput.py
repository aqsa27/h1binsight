# -*- coding: utf-8 -*-
class ParseInput():
    def __init__(self, file,sep):
        self.filename = file
        self.occupations, self.states,self.certified_count = {},{},0
        self.sep = sep
        self.content = None
        self.status_ind, self.socname_ind, self.state_ind = None, None, None
        
    def readfile(self):
        with open(self.filename) as f:
            s = f.read()
        self.content = s.split("\n") #split row by row
       
    def headerIndex(self):  #get index of needed fields from 1st line of the file
        for i,j in enumerate(self.content[0].split(';')):
            if j == "CASE_STATUS":
                self.status_ind = i
            if j == "SOC_NAME":
                self.socname_ind = i
            if j == "WORKSITE_STATE":
                self.state_ind = i
    
    def parseRow(self, line):
        if line == "":  #if its empty row return None
            return None
        fields = line.split(self.sep) 
        status = fields[self.status_ind].strip()
        status = status.upper()
        occupation = fields[self.socname_ind].strip()
        occupation = occupation.upper()
        state = fields[self.state_ind].strip()
        state = state.upper()

        if status == 'CERTIFIED': #if application is certified parse fields
            #get occupation name and increase the count
            if occupation in self.occupations:
                tmpsoc = self.occupations[occupation]
                tmpsoc +=1
                self.occupations[occupation] = tmpsoc
            else:
                self.occupations[occupation] = 1 
            #get state name and increase the count
            if state in self.states:
                tmpsoc = self.states[state]
                tmpsoc +=1
                self.states[state] = tmpsoc
            else:
                self.states[state] = 1 
            self.certified_count +=1 #increase the count of certified applications
            
    def getData(self):
        self.readfile()
        self.headerIndex()
        list(map(self.parseRow,self.content[1:])) #from 2nd line apply map to parse row fields and get distribution
        return  self.certified_count, self.occupations, self.states

