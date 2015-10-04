//
//  ConexDAO.h
//  conex
//
//  Created by Gleison on 02/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "sqlite3.h"

@interface ConexDAO : NSObject{
    sqlite3 *db;
}
-(NSString *) filePath;
-(void) openDB;
-(void) createTable;
-(NSMutableArray *) getAllRowsFromTable;
-(int) insertRecord:(Emprestimo *) emprestimo;
@end
