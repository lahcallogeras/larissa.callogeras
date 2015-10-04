//
//  ContatoController.m
//  conex
//
//  Created by Gleison on 08/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import "ContatoController.h"
#import "Contato.h"
#import "ContatoDAO.h"
#import "Globais.h"
#import "ViewController.h"
#import "Usuario.h"
#import "Reachability.h"
#import "ConexaoWebService.h"
#import "SBJson.h"


@interface ContatoController ()

@end

@implementation ContatoController

@synthesize contatosTableView;



-(IBAction)btnSair:(id)sender{
    exit(0);
   
}

-(IBAction)BtnDesconectar:(id)sender{

    UsuarioDAO *usuDAO = [[UsuarioDAO alloc] init];    
    [usuDAO limpaManterConectado];
    [self performSegueWithIdentifier: @"viewLogin" sender: self];

}

- (IBAction)gerenciarContatos:(id)sender{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.newconex.heliohost.org/contatos.php"]];
    
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

    //Timer
    [NSTimer scheduledTimerWithTimeInterval:60.0 target: self selector: @selector(recebeMensagem:) userInfo: nil repeats: YES];
   
    //fim timer
    [self carregaContatos];
    
    NSLog(@"%@",[Usuario usuarioGlobal].usuarioUsu);
    

    
}

- (void) carregaContatos
{
    
    ContatoDAO *listaContato =[[ContatoDAO alloc] init];
    lista = [listaContato getContatos:[Usuario usuarioGlobal].codigoUsu];
    [self.tableView reloadData];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
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
    static NSString *cellID = @"codigoCtt";
    
    UITableViewCell *cell = [self.contatosTableView dequeueReusableCellWithIdentifier:cellID];
    
    if ( cell == nil )
    {
        // Com StyleDefault, aparece só o título, sem a descrição
        //cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellID];
        
        // Com StyleSubtitle, são mostrados título e descrição
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cellID];
    }
    
    
    Contato *contatoObj = [lista objectAtIndex:[indexPath row]];
    cell.textLabel.text = contatoObj.nomeUsuCtt;
    
    if(contatoObj.qtdMsg > 0){
        
          cell.detailTextLabel.textColor = [UIColor redColor];
        
        cell.detailTextLabel.textAlignment = UITextAlignmentCenter;
        if (contatoObj.qtdMsg > 1){
            NSString * msg = [NSString stringWithFormat:@"%d Novas Mensagens" , contatoObj.qtdMsg];          
            cell.detailTextLabel.text   = msg;
            //cell.detailTextLabel.frame = CGRectMake(0, self.detailTextLabel.frame.origin.y, self.frame.size.width, self.detailTextLabel.frame.size.height);
        }
        else
             cell.detailTextLabel.text   = @"1 Nova Mensagem";
        
    }
    //cell.detailTextLabel.text = [thing description];
    
    // Adiciona imagem à célula
    
    //busca imagem
   if(![contatoObj.nomeFotoCtt isEqualToString: @"anonimo.png"]){
       
       NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
       NSString *documents = [paths objectAtIndex:0];
       NSString *finalPath = [documents stringByAppendingPathComponent:contatoObj.nomeFotoCtt];
       NSData *pngData = [NSData dataWithContentsOfFile:finalPath];
       UIImage *contatoFto = [UIImage imageWithData:pngData];
       //image.image = contatoFto;

       CGSize newSize = CGSizeMake(600, 600);  //whaterver size
       UIGraphicsBeginImageContext(newSize);
       [contatoFto drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
       UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
       cell.imageView.image = newImage;
   
   }
   else
   {
       CGSize newSize = CGSizeMake(600, 600);  //whaterver size
       UIGraphicsBeginImageContext(newSize);
       [[UIImage imageNamed: @"anonimo.png"] drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
       UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
       cell.imageView.image =  newImage;
   }

    
        
    

    
    
    
    //cell.imageView.image = [UIImage imageNamed:@"imagem.png"];
    
    return cell;

    }
    
#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    Contato  *contatoObj = [lista objectAtIndex:[indexPath row]];
    
    [[Contato contatoGlobal] funContatoGlobal:contatoObj];
    
    
    /*
    NSString *msg = [NSString stringWithFormat:@"Você selecionou o contato: %@", contatoObj.nomeUsuCtt];
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Seleção"
                                                    message:msg
                                                   delegate:nil
                                          cancelButtonTitle:@"Eu sei disso"
                                          otherButtonTitles:nil];
    [alert show];
    */
    [self.contatosTableView deselectRowAtIndexPath:indexPath animated:YES];
     
    [self performSegueWithIdentifier: @"msgView" sender: self];


}





//fim teste timer
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
            
        }
        [self carregaContatos];
        
    }
    
}


@end
