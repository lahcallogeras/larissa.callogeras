//
//  MensagemController.m
//  conex
//
//  Created by Gleison on 10/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "MensagemController.h"
#import "MensagemDAO.h"
#import "Usuario.h"
#import "Contato.h"
#import "MsgCell.h"
#import "Mensagem.h"
#import "Reachability.h"
#import "ConexaoWebService.h"
#import "SBJson.h"





@interface MensagemController ()

@end

@implementation MensagemController

- (IBAction)btnVoltar:(id)sender{
     [self performSegueWithIdentifier: @"contatoView" sender: self];
}

- (IBAction)btnExcluir:(id)sender{
    MensagemDAO *mensagemDAO =[[MensagemDAO alloc] init];
    [mensagemDAO  deleteMensagem:[Contato contatoGlobal].codigoUsuCtt, [Usuario usuarioGlobal].codigoUsu];
    [self carregaMensagem];
}


- (IBAction)doneTeclado:(id)sender{
    [sender resignFirstResponder];
}

- (IBAction)ativaTeclado:(id)sender{
    
    CGFloat height = (self.tableView.frame.size.height/1.8);
    CGFloat width = self.tableView.frame.size.width;
    
    CGRect halfFrame = CGRectMake(0, 0, width, height);
    self.tableView.frame = halfFrame;
    [self setFooterTableView];    
}

- (IBAction)desativaTeclado:(id)sender{
    CGFloat height = (self.tableView.frame.size.height*1.8);
    CGFloat width = self.tableView.frame.size.width;
    
    CGRect fullFrame = CGRectMake(0, 0, width, height);
    self.tableView.frame = fullFrame;
    [self setFooterTableView];

}

- (void) alertStatus:(NSString *)msg :(NSString *)title
{
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:title
                                                        message:msg
                                                       delegate:self
                                              cancelButtonTitle:@"Ok"
                                              otherButtonTitles:nil, nil];
    
    [alertView show];
}

- (void) setFooterTableView
{
    CGRect footerBounds = [self.tableView.tableFooterView bounds];
    CGRect footerRectInTable = [self.tableView convertRect:footerBounds fromView:self.tableView.tableFooterView];
    [self.tableView scrollRectToVisible:footerRectInTable animated:YES];
}

- (void) carregaMensagem
{
    
    MensagemDAO *mensagemDAO =[[MensagemDAO alloc] init];
    lista = [mensagemDAO getMensagem:[Usuario usuarioGlobal].codigoUsu,[Contato contatoGlobal].codigoUsuCtt];
    
    [self.tableView  reloadData];
    [self setFooterTableView];
   
}

- (void) recebeMensagem:(NSTimer*) t
{
    MensagemDAO *mensagemDAO =[[MensagemDAO alloc] init];
    // allocate a reachability object
    Reachability* reach = [Reachability reachabilityWithHostname:@"www.google.com"];
    
    NetworkStatus netStatus = [reach currentReachabilityStatus];
    if(netStatus != NotReachable){
        
        NSString *codigoUsu = [NSString stringWithFormat:@"%d",[Usuario usuarioGlobal].codigoUsu];
        NSString *codigoContato = [NSString stringWithFormat:@"%d",[Contato contatoGlobal].codigoUsuCtt];
        
        NSURL *url=[NSURL URLWithString:@"http://www.newconex.heliohost.org/recebeMsgA.php"];
        NSString *post =[[NSString alloc] initWithFormat:@"P1=%@&P2=%@",codigoUsu, codigoContato];
        
        ConexaoWebService *conectaWS = [[ConexaoWebService alloc]init];
        NSString* responseData = [conectaWS ConectaWS:post, url];
        
        SBJsonParser *jsonParser = [SBJsonParser new];
        NSDictionary *jsonData = (NSDictionary *) [jsonParser objectWithString:responseData error:nil];
        
        NSInteger sucesso = [(NSNumber *) [jsonData objectForKey:@"sucesso"] integerValue];
        NSLog(@"%d",sucesso);
        if(sucesso == 1)
        {
            
            ContatoDAO *contatoDAO =[[ContatoDAO alloc] init];
            NSArray* mensagemJRecJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"msg"];
            
            for (int i = 0; i < [mensagemJRecJson count]; i++) {
                Mensagem *msg;
                msg = [[Mensagem alloc] init];
                NSDictionary* msgData = [mensagemJRecJson objectAtIndex:i];
                msg.codigoMsg = [(NSNumber *) [msgData objectForKey:@"codigoMsg"] integerValue];
                msg.usuOrig = [contatoDAO getContatoId:[(NSNumber *) [msgData objectForKey:@"codigoUsuOriMsg"] integerValue]];
                msg.dataMsg = [msgData objectForKey:@"dataMsg"];
                msg.textoMsg = [msgData objectForKey:@"textoMsg"];
                msg.statusLida = @"N";
                [mensagemDAO  insertMensagem:msg, [Usuario usuarioGlobal].codigoUsu];
                
            }
            
            [self carregaMensagem];
        
        }

    
    }
    
}


- (IBAction)btnEnviarClicked:(id)sender {
    
    if([[txtMsg text] isEqualToString:@""]) {
        [self alertStatus:@"Por favor informe a mensagem!" :@"Aviso!"];
    } else {
        
        MensagemDAO *mensagemDAO =[[MensagemDAO alloc] init];
        // allocate a reachability object
        Reachability* reach = [Reachability reachabilityWithHostname:@"www.google.com"];
        
        NetworkStatus netStatus = [reach currentReachabilityStatus];
        if(netStatus == NotReachable){
            [self alertStatus:@"Sem conexao com a internet, nao e possivel enviar esta mensagem!" :@"Aviso!"];
        } else{
            
            NSString *codigoUsu = [NSString stringWithFormat:@"%d",[Usuario usuarioGlobal].codigoUsu];
            NSString *codigoContato = [NSString stringWithFormat:@"%d",[Contato contatoGlobal].codigoUsuCtt];
            
            NSURL *url=[NSURL URLWithString:@"http://www.newconex.heliohost.org/enviaMsgA.php"];
            NSString *post =[[NSString alloc] initWithFormat:@"P1=%@&P2=%@&P3=%@",codigoUsu, codigoContato,[txtMsg text]];
            
            NSLog(@"%@",[txtMsg text]);
            NSLog(@"%@",post);
            
            ConexaoWebService *conectaWS = [[ConexaoWebService alloc]init];
            NSString* responseData = [conectaWS ConectaWS:post, url];
            
            SBJsonParser *jsonParser = [SBJsonParser new];
            NSDictionary *jsonData = (NSDictionary *) [jsonParser objectWithString:responseData error:nil];            
            
            NSInteger sucesso = [(NSNumber *) [jsonData objectForKey:@"sucesso"] integerValue];
            NSLog(@"%d",sucesso);
            if(sucesso == 1)
            {
                ContatoDAO *contatoDAO =[[ContatoDAO alloc] init];
                
                
                NSArray* mensagemJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"msg"];
                
                for (int i = 0; i < [mensagemJson count]; i++) {
                    Mensagem *msg;
                    msg = [[Mensagem alloc] init];
                    NSDictionary* msgData = [mensagemJson objectAtIndex:i];                    
                    msg.codigoMsg = [(NSNumber *) [msgData objectForKey:@"codigoMsg"] integerValue];
                    msg.usuDestino = [contatoDAO getContatoId:[(NSNumber *) [msgData objectForKey:@"codigoUsuDestMsg"] integerValue]];
                    msg.dataMsg = [msgData objectForKey:@"dataMsg"];
                    msg.textoMsg = [msgData objectForKey:@"textoMsg"];
                    
                    NSLog(@"%@",msg.textoMsg);
                    
                    msg.statusLida = @"S";
                    [mensagemDAO  insertMensagem:msg, [Usuario usuarioGlobal].codigoUsu];
                    
                }
                
                
                NSArray* mensagemJRecJson = [(NSDictionary*) [responseData JSONValue] objectForKey:@"msgRec"];
                
                for (int i = 0; i < [mensagemJRecJson count]; i++) {
                    Mensagem *msg;
                    msg = [[Mensagem alloc] init];
                    NSDictionary* msgData = [mensagemJRecJson objectAtIndex:i];
                    msg.codigoMsg = [(NSNumber *) [msgData objectForKey:@"codigoMsg"] integerValue];
                    msg.usuOrig = [contatoDAO getContatoId:[(NSNumber *) [msgData objectForKey:@"codigoUsuOriMsg"] integerValue]];
                    msg.dataMsg = [msgData objectForKey:@"dataMsg"];
                    msg.textoMsg = [msgData objectForKey:@"textoMsg"];
                    msg.statusLida = @"N";
                    [mensagemDAO  insertMensagem:msg, [Usuario usuarioGlobal].codigoUsu];
                    
                }
                
                txtMsg.text = @"";
                
                [self carregaMensagem];
                
            
            }
            else{
                [self alertStatus:@"Problemas ao enviar mensagem!" :@"Aviso!"];
            
            }           
        }
        
    
    }
    
 
    

}


- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];    
    [self carregaMensagem];
    //Timer
    [NSTimer scheduledTimerWithTimeInterval: 5.0 target: self selector: @selector(recebeMensagem:) userInfo: nil repeats: YES];
    //fim timer

}

- (void)viewDidUnload {
    // unregister for keyboard notifications while not visible.
}



- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}



#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
       

    int teste = [lista count];
    return teste;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    
    static NSString *cellID = @"codigoMsg";
    
    
    
    Mensagem *msgObj = [lista objectAtIndex:[indexPath row]];
       
    
    
    MsgCell *cell = (MsgCell *) [tableView dequeueReusableCellWithIdentifier:cellID];
    
    
    NSDateFormatter * formato = [[NSDateFormatter alloc] init];
    [formato setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *dataMsgD = [formato dateFromString: msgObj.dataMsg];
    
    NSLog(@"%@",msgObj.dataMsg);
    NSLog(@"%@",dataMsgD);
    
    formato = [[NSDateFormatter alloc] init];
    [formato setDateFormat:@"dd/MM/yyyy HH:mm"];
    
    
    NSString *dataMsgS = [formato stringFromDate:dataMsgD];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documents = [paths objectAtIndex:0];
    if(msgObj.usuOrig != nil)
    {
        
       
        NSString *finalPath = [documents stringByAppendingPathComponent:msgObj.usuOrig.nomeFotoCtt];
        NSData *pngData = [NSData dataWithContentsOfFile:finalPath];
        UIImage *contatoFto = [UIImage imageWithData:pngData];
        CGSize newSize = CGSizeMake(600, 600);  //whaterver size
        UIGraphicsBeginImageContext(newSize);
        [contatoFto drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
        UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();

        
        NSArray* views = [[NSBundle mainBundle] loadNibNamed:@"MsgCellLeft" owner:nil options:nil];
        
        for (UIView *view in views) {
            if([view isKindOfClass:[UITableViewCell class]])
            {
                cell = (MsgCell*)view;
            }
        }
        
        
        [[cell lblMsgLeft] setText:msgObj.textoMsg];
        [[cell lblDataLeft] setText:dataMsgS];
        [[cell imgFotoLeft] setImage:newImage];
                
    }
    else
    {
        
        NSString *finalPath = [documents stringByAppendingPathComponent:[Usuario usuarioGlobal].fotoUsu];
        NSData *pngData = [NSData dataWithContentsOfFile:finalPath];
        UIImage *contatoFto = [UIImage imageWithData:pngData];
        CGSize newSize = CGSizeMake(600, 600);  //whaterver size
        UIGraphicsBeginImageContext(newSize);
        [contatoFto drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
        UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
        
        
        NSArray* views = [[NSBundle mainBundle] loadNibNamed:@"MsgCellRight" owner:nil options:nil];
        
        for (UIView *view in views) {
            if([view isKindOfClass:[UITableViewCell class]])
            {
                cell = (MsgCell*)view;
            }
        }
        
        
        
        [[cell lblMsgRight] setTextAlignment:UITextAlignmentRight];
        [[cell lblMsgRight] setText:msgObj.textoMsg];
        
        [[cell lblDataRight] setTextAlignment:UITextAlignmentRight];        
        [[cell lblDataRight] setText:dataMsgS];
        
        [[cell imgFotoRight] setImage:newImage];
        
    
    }
    
     
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];

}

@end
