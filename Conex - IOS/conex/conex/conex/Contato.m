//
//  Contatos.m
//  conex
//
//  Created by Gleison on 05/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "Contato.h"

@implementation Contato

@synthesize  usuarioUsuCtt,statusCtt, nomeUsuCtt, dataNascUsuCtt, emailUsuCtt, sexoUsuCtt, dataCadUsuCtt, nomeFotoCtt, codigoCtt, codigoUsuLocalCtt,codigoUsuCtt;


//Funçoes contato Global
static Contato *contatoGlobal = nil;

+ (Contato*)contatoGlobal {
    if (contatoGlobal == nil) {
        contatoGlobal = [[super allocWithZone:NULL] init];
        
        // initialize your variables here
        contatoGlobal.codigoUsuCtt = 0;
    }
    return contatoGlobal;
}



-(void)funContatoGlobal:(Contato*)contato;{
    self.codigoCtt = contato.codigoCtt;
    self.codigoUsuLocalCtt = contato.codigoUsuLocalCtt;
    self.codigoUsuCtt = contato.codigoUsuCtt;
    /*
    self.statusCtt = contato.statusCtt;
    self.usuarioUsuCtt = contato.usuarioUsuCtt;
    self.nomeUsuCtt = contato.nomeUsuCtt;
    self.dataNascUsuCtt = contato.dataNascUsuCtt;
    self.emailUsuCtt = contato.emailUsuCtt;
    self.sexoUsuCtt = contato.sexoUsuCtt;
    self.dataCadUsuCtt = contato.dataCadUsuCtt;
    self.nomeFotoCtt = contato.nomeFotoCtt;
     */  
}






@end

